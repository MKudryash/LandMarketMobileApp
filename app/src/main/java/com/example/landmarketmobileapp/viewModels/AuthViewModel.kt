package com.example.landmarketmobileapp.viewModels

import android.content.Context
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constants.supabase
import com.example.landmarketmobileapp.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel: ViewModel() {

    // Константы для SharedPreferences
    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_EMAIL = "saved_email"
        private const val KEY_PASSWORD = "saved_password"
        private const val KEY_REMEMBER_ME = "remember_me"
    }

    private val _uiState = mutableStateOf(SignInState())
    val uiState: SignInState get() = _uiState.value

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val _uiStateSignUp = mutableStateOf(SignUpState())
    val uiStateSignUp: SignUpState get() = _uiStateSignUp.value

    private val _resultStateSignUp = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultStateSignUp: StateFlow<ResultState> = _resultStateSignUp.asStateFlow()

    // Состояния для управления экраном
    private val _isInitialCheckInProgress = MutableStateFlow(true) // Начальная проверка
    val isInitialCheckInProgress = _isInitialCheckInProgress.asStateFlow()

    private val _isAutoLoginInProgress = MutableStateFlow(false) // Автоматический вход
    val isAutoLoginInProgress = _isAutoLoginInProgress.asStateFlow()

    // Метод для начальной проверки
    fun performInitialCheck(context: Context) {
        _isInitialCheckInProgress.value = true

        viewModelScope.launch {
            try {
                // Загружаем сохраненные данные
                loadSavedCredentials(context)

                // Небольшая задержка для плавности (опционально)
                kotlinx.coroutines.delay(300)

                // Проверяем, есть ли сохраненные данные для автоматического входа
                if (hasSavedCredentials(context)) {
                    // Если есть сохраненные данные, выполняем автоматический вход
                    performAutoLogin(context)
                } else {
                    // Если нет сохраненных данных, показываем экран входа
                    _isInitialCheckInProgress.value = false
                }

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Ошибка при начальной проверке: ${e.message}")
                _isInitialCheckInProgress.value = false
            }
        }
    }

    // Метод для проверки наличия сохраненных учетных данных
    fun hasSavedCredentials(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val password = prefs.getString(KEY_PASSWORD, "") ?: ""
        val rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false)

        // Возвращаем true только если rememberMe включен И есть email и пароль
        return rememberMe && email.isNotEmpty() && password.isNotEmpty()
    }

    // Метод для загрузки сохраненных данных
    fun loadSavedCredentials(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedEmail = prefs.getString(KEY_EMAIL, "")
        val savedPassword = prefs.getString(KEY_PASSWORD, "")
        val rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false)

        _uiState.value = _uiState.value.copy(
            email = savedEmail ?: "",
            password = savedPassword ?: "",
            rememberMe = rememberMe
        )

        // Проверяем валидность email после загрузки
        _uiState.value.errorEmail = _uiState.value.email.isEmailValid()
    }

    // Метод для выполнения автоматического входа
    fun performAutoLogin(context: Context) {
        _isAutoLoginInProgress.value = true
        _resultState.value = ResultState.Loading

        viewModelScope.launch {
            try {
                supabase.auth.signInWith(Email) {
                    email = _uiState.value.email
                    password = _uiState.value.password
                }
                Log.d("AutoLogin", "Success")

                // Успешный автоматический вход
                _resultState.value = ResultState.Success("Auto login successful")

            } catch (_ex: AuthRestException) {
                Log.d("AutoLogin", "Failed: ${_ex.message}")

                // Если автоматический вход не удался, сбрасываем состояния
                _isAutoLoginInProgress.value = false
                _isInitialCheckInProgress.value = false
                _resultState.value = ResultState.Initialized

            } catch (e: Exception) {
                Log.d("AutoLogin", "Failed: ${e.message}")
                _isAutoLoginInProgress.value = false
                _isInitialCheckInProgress.value = false
                _resultState.value = ResultState.Initialized
            }
        }
    }

    // Метод для сохранения логина и пароля
    fun saveCredentials(context: Context, email: String, password: String, rememberMe: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        prefs.edit {
            if (rememberMe) {
                putString(KEY_EMAIL, email)
                putString(KEY_PASSWORD, password)
            } else {
                remove(KEY_EMAIL)
                remove(KEY_PASSWORD)
            }
            putBoolean(KEY_REMEMBER_ME, rememberMe)
        }
    }

    // Метод для очистки сохраненных данных
    fun clearSavedCredentials(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            clear()
        }
    }

    // Метод для получения сохраненного email
    fun getSavedEmail(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, "") ?: ""
    }

    // Метод для получения сохраненного пароля
    fun getSavedPassword(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PASSWORD, "") ?: ""
    }

    // Метод для проверки, включен ли rememberMe
    fun isRememberMeEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }

    fun updateState(newState: SignInState) {
        _uiState.value = newState
        _uiState.value.errorEmail = _uiState.value.email.isEmailValid()
        _resultState.value = ResultState.Initialized
    }

    fun updateState(newState: SignUpState) {
        _uiStateSignUp.value = newState
        _uiStateSignUp.value.isEmailError = _uiStateSignUp.value.email.isEmailValid()
        _resultStateSignUp.value = ResultState.Initialized
    }

    fun signIn(context: Context) {
        _resultState.value = ResultState.Loading

        if (!_uiState.value.errorEmail) {
            _resultState.value = ResultState.Error("Ошибка ввода почты")
            return
        }

        viewModelScope.launch {
            try {
                supabase.auth.signInWith(Email) {
                    email = _uiState.value.email
                    password = _uiState.value.password
                }
                Log.d("SignIn", "Success")

                // Сохраняем данные если включен rememberMe
                saveCredentials(
                    context,
                    _uiState.value.email,
                    _uiState.value.password,
                    _uiState.value.rememberMe
                )

                _resultState.value = ResultState.Success("Success")

            } catch (_ex: AuthRestException) {
                Log.d("SignIn", _ex.message.toString())
                Log.d("SignIn", _ex.errorCode.toString())

                _resultState.value = ResultState.Error(_ex.description ?: "Ошибка получения данных")
            }
        }
    }

    fun signUp() {
        _resultStateSignUp.value = ResultState.Loading
        if (_uiStateSignUp.value.isEmailError && _uiStateSignUp.value.password == _uiStateSignUp.value.confirmPassword) {
            viewModelScope.launch {
                try {
                    supabase.auth.signUpWith(Email) {
                        email = _uiStateSignUp.value.email
                        password = _uiStateSignUp.value.password
                    }
                    Log.d("SignUp", "Success")
                    val user = Profile(
                        null,
                        _uiStateSignUp.value.username,
                        _uiStateSignUp.value.email,
                        _uiStateSignUp.value.telephone,
                        "",
                        0.0
                    )
                    supabase.from("users").insert(user)
                    _resultStateSignUp.value = ResultState.Success("Success")
                } catch (_ex: AuthRestException) {
                    Log.d("signUp", _ex.message.toString())
                    Log.d("signUp", _ex.errorCode.toString())
                    Log.d("signUp", _ex.errorDescription.toString())

                    _resultStateSignUp.value = ResultState.Error(_ex.errorDescription ?: "Ошибка получения данных")
                }
            }
        } else {
            _resultStateSignUp.value = ResultState.Error("Ошибка ввода почты")
        }
    }
}

sealed class ResultState {
    data object Loading : ResultState()
    data object Initialized : ResultState()
    data class Success(val message: String) : ResultState()
    data class Error(val message: String) : ResultState()
}

data class SignInState(
    val email: String = "t@t.ru",
    val password: String = "1",
    var errorEmail: Boolean = false,
    val errorPassword: Boolean = false,
    val rememberMe: Boolean = false // Добавлено поле для запоминания данных
)

data class SignUpState(
    val email: String = "",
    val telephone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    var isEmailError: Boolean = false
)

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

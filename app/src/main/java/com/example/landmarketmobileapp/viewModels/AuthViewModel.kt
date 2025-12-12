package com.example.landmarketmobileapp.viewModels

import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constatnt
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel: ViewModel() {

    private val _uiState = mutableStateOf(SignInState())
    val uiState: SignInState get() = _uiState.value

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    fun updateState(newState: SignInState) {
        _uiState.value = newState
        _uiState.value.errorEmail = _uiState.value.email.isEmailValid()
        _resultState.value = ResultState.Initialized
    }
    fun signIn() {
        _resultState.value = ResultState.Loading
        if (_uiState.value.errorEmail) {
            viewModelScope.launch {
                try {
                    Constatnt.supabase.auth.signInWith(Email)
                    {
                        email = _uiState.value.email
                        password = _uiState.value.password
                    }
                    Log.d("SignIn", "Success")

                    _resultState.value = ResultState.Success("Success")
                } catch (_ex: AuthRestException) {
                    Log.d("SignIn", _ex.message.toString())
                    Log.d("SignIn", _ex.errorCode.toString())

                    _resultState.value = ResultState.Error(_ex.description ?: "Ошибка получения данных")
                }
            }
        }
        else{
            _resultState.value = ResultState.Error( "Ошибка ввода почты")
        }
    }
}
sealed class ResultState {
    data object Loading : ResultState() // Это объект, представляющий состояние загрузки
    data object Initialized : ResultState() //  Это объект, представляющий состояние инициализации
    data class Success(val message: String) : ResultState() //  Это класс, представляющий успешное завершение операции.  Он содержит свойство message типа String, которое может содержать дополнительную информацию о результате
    data class Error(val message: String) : ResultState() //  Это класс, представляющий ошибку.  Он также содержит свойство message типа String,  которое описывает произошедшую ошибку
}
data class SignInState (
    val email: String = "",
    val password: String = "",
    var errorEmail:Boolean = false,
    val errorPassword:Boolean = false
)
fun String.isEmailValid () : Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

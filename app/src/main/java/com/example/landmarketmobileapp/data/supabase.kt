package com.example.landmarketmobileapp.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object Constants {
    val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://pqkedukspphlsgsflzzj.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBxa2VkdWtzcHBobHNnc2ZsenpqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU1MjY4MDksImV4cCI6MjA4MTEwMjgwOX0.Xu9HjTxph4eXfma7PAYDvoESZrGm5_PhR2td65ldq_A"
    ) {
        // Установите Postgrest для работы с базой данных
        install(Postgrest)

        // Настройка аутентификации
        install(io.github.jan.supabase.auth.Auth) {

            alwaysAutoRefresh = true
            autoLoadFromStorage = true
        }
    }
}
package uk.ac.tees.mad.weatherly.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage


object SupabaseClientProvider {
    val client =
        createSupabaseClient(
            supabaseUrl = "https://xpcsvomqvzbqjkauwrre.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhwY3N2b21xdnpicWprYXV3cnJlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTkxMzkwNzIsImV4cCI6MjA3NDcxNTA3Mn0.Szi9DImpu3R0MO-0qYm951Fd7OlitgFbUXvKtINHX8g"
        ) {
            install(GoTrue)
            install(Storage)
        }

}
package uk.ac.tees.mad.weatherly.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage


object SupabaseClientProvider {
    val client =
        createSupabaseClient(
            supabaseUrl = "https://hkvmlgzfphjlozjnrhlb.supabase.co",
            supabaseKey = "sb_publishable_rdUVCUtD6soOYv6Ri6slLw_7aOsvXzs"
        ) {
            install(GoTrue)
            install(Storage)
        }

}
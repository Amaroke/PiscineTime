package com.example.piscinetime

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class PiscineScheduleScraper {
    private val okHttpClient = OkHttpClient()

    suspend fun scrapePiscineSchedule(): String {
        Log.d("PiscineScheduleScraper", "Début du scraping")
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://www.grandnancy.eu/sortir-decouvrir/piscines/piscine-de-laxou")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw Exception("Requête HTTP a échoué avec le code ${response.code}")
                }

                val responseBody = response.body
                if (responseBody != null) {
                    val responseText = responseBody.string()
                    val document = Jsoup.parse(responseText)
                    val scheduleElements = document.select("p.bloc-infos-pratiques")
                    val scheduleText = scheduleElements.subList(3, scheduleElements.size)
                        .joinToString(separator = "\n") { it.text() }
                    scheduleText
                } else {
                    throw Exception("Réponse vide du serveur")
                }
            } catch (e: Exception) {
                Log.e("PiscineScheduleScraper", "Erreur lors du scraping : ${e.message}", e)
                ""
            }
        }
    }
}

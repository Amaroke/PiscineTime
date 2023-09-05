package com.example.piscinetime

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class ScrapperMichel {
    private val okHttpClient = OkHttpClient()

    suspend fun scrapePiscineSchedule(): String {
        Log.d("PiscineScheduleScraper", "Début du scraping")
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://www.grandnancy.eu/sortir-decouvrir/piscines/piscine-michel-bertrand-a-vandoeuvre-les-nancy")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw Exception("Requête HTTP a échoué avec le code ${response.code}")
                }

                val responseBody = response.body
                if (responseBody != null) {
                    val responseText = responseBody.string()
                    val document = Jsoup.parse(responseText)

                    document.select("em:containsOwn(Evacuation du bassin 30 mns avant la fermeture)")
                        .remove()

                    val scheduleElements = document.select("p.bloc-infos-pratiques")
                    val filteredLines = StringBuilder()
                    var ignoreLines = false

                    for (element in scheduleElements.subList(1, scheduleElements.size)) {
                        val htmlContent = element.html()
                        if (htmlContent.startsWith("<br>", ignoreCase = true)) {
                            ignoreLines = false
                        } else if (!ignoreLines) {
                            if (!htmlContent.startsWith("Evacuation", ignoreCase = true) &&
                                !htmlContent.startsWith("Réserver", ignoreCase = true) &&
                                !htmlContent.startsWith("Fréquentation", ignoreCase = true)
                            ) {
                                filteredLines.append(htmlContent).append("\n")
                            } else {
                                ignoreLines = true
                            }
                        }
                    }

                    filteredLines.toString()
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

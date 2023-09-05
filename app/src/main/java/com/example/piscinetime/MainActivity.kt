package com.example.piscinetime

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.piscinetime.ui.theme.PiscineTimeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PiscineTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var piscineSchedule: String by remember { mutableStateOf("") }
                    var piscineSchedule2: String by remember { mutableStateOf("") }

                    LaunchedEffect(Unit) {
                        val newPiscineSchedule = withContext(Dispatchers.IO) {
                            val scraper = ScrapperLaxou()
                            scraper.scrapePiscineSchedule()
                        }
                        val newPiscineSchedule2 = withContext(Dispatchers.IO) {
                            val scraper = ScrapperMichel()
                            scraper.scrapePiscineSchedule()
                        }
                        piscineSchedule = newPiscineSchedule
                        piscineSchedule2 = newPiscineSchedule2
                    }


                    if (piscineSchedule.isNotEmpty() && piscineSchedule2.isNotEmpty()) {
                        WebViewContainer(htmlContent = piscineSchedule + piscineSchedule2)
                    }
                }
            }
        }
    }
}

@Composable
fun WebViewContainer(htmlContent: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val webView = WebView(context)
            true.also { webView.settings.javaScriptEnabled = it }
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            webView
        }
    )
}

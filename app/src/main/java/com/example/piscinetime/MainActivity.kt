package com.example.piscinetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.piscinetime.ui.theme.PiscineTimeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
                    var piscineSchedule: String

                    runBlocking {
                        piscineSchedule = withContext(Dispatchers.IO) {
                            val scraper = PiscineScheduleScraper()
                            scraper.scrapePiscineSchedule()
                        }
                    }

                    Text(
                        text = "Laxou :\n$piscineSchedule",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

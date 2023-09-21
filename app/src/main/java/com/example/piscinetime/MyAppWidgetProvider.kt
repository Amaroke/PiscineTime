package com.example.piscinetime

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews

private const val HTML_CONTENT_PREF_KEY = "html_content_pref_key"


class MyAppWidgetProvider : AppWidgetProvider()  {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Retrieve the HTML content from shared preferences
        val htmlContent = getHtmlContentFromSharedPreferences(context)
        Log.d("FDP", "HTML Content: $htmlContent")

        // Parcourez tous les widgets actifs et mettez à jour leur contenu.
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, htmlContent)
        }
    }

    // Function to retrieve HTML content from shared preferences
    private fun getHtmlContentFromSharedPreferences(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "MySharedPrefs", Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(HTML_CONTENT_PREF_KEY, "") ?: ""
    }
}


private fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    htmlContent: String
) {
    val views = RemoteViews(
        context.packageName,
        R.layout.my_app_widget_layout
    ) // Remplacez "my_app_widget_layout" par le nom de votre propre mise en page de widget.
    views.setTextViewText(
        R.id.widgetTextView,
        htmlContent
    ) // Remplacez R.id.widgetTextView par l'ID de votre TextView dans la mise en page du widget.

    // Mettez en place une intention pour ouvrir votre application lorsque le widget est cliqué (si nécessaire).

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

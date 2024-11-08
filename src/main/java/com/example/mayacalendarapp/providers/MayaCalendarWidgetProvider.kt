package com.example.mayacalendarapp.providers

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mayacalendarapp.MainActivity
import com.example.mayacalendarapp.R
import com.example.mayacalendarapp.data.CalendarDataRepository
import com.example.mayacalendarapp.data.Tzolkin
import com.example.mayacalendarapp.dataStore
import com.example.mayacalendarapp.databinding.CalendarWidgetBinding
import kotlinx.coroutines.runBlocking


class MayaCalendarWidgetProvider : AppWidgetProvider() {
    private lateinit var dataStoreRepository : CalendarDataRepository
    private lateinit var tzolkin : Tzolkin

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        runBlocking {
            dataStoreRepository = CalendarDataRepository(context.dataStore)
            tzolkin = Tzolkin(dataStoreRepository)
        }
        appWidgetIds.forEach { appWidgetId ->
            updateCalendarWidget(context, appWidgetManager, appWidgetId)
        }
    }
    private fun updateCalendarWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId : Int
    ) {
        val calendarRound = tzolkin.getCalendarRound()
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val views: RemoteViews = RemoteViews(
            context.packageName,
            R.layout.calendar_widget
        ).apply {
            setOnClickPendingIntent(R.id.Main, pendingIntent)
            setTextViewText(R.id.CalendarRoundWidget, calendarRound)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
package com.example.mayacalendarapp.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.datastore.dataStore
import com.example.mayacalendarapp.MainActivity
import com.example.mayacalendarapp.R
import com.example.mayacalendarapp.data.Tzolkin
import com.example.mayacalendarapp.dataStore


class MayaCalendarWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateCalendarWidget(context, appWidgetManager, appWidgetId)
        }
    }
    private fun updateCalendarWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId : Int
    ) {
        lateinit var dataStoreRepository : CalendarDataRepository
        val tzolkin : Tzolkin = Tzolkin()
        val longCountText = tzolkin.getLongCount()
        val calendarDay = tzolkin.getCalendarDay()
        val calendarMonth = tzolkin.getCalendarMonth()

        val views: RemoteViews = RemoteViews(
            context.packageName,
            R.layout.calendar_widget
        ).apply {
            setTextViewText(R.id.LongCountWidget, longCountText)
            setTextViewText(R.id.CalendarDay, calendarDay)
            setTextViewText(R.id.CalendarMonth, calendarMonth)
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
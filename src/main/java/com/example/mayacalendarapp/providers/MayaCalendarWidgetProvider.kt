package com.example.mayacalendarapp.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mayacalendarapp.MainActivity
import com.example.mayacalendarapp.R
import com.example.mayacalendarapp.Tzolkin

class MayaCalendarWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId : Int
) {
    val tzolkin = Tzolkin.getInstance()
    tzolkin.calculate()

    val longCountText = tzolkin.getLongCount()
    val calendarDay = tzolkin.getCalendarDay()
    val calendarMonth = tzolkin.getCalendarMonth()

    val views : RemoteViews = RemoteViews(
        context.packageName,
        R.layout.calendar_widget
    ).apply {
        setTextViewText(R.id.LongCountWidget, longCountText)
        setTextViewText(R.id.CalendarDay, calendarDay)
        setTextViewText(R.id.CalendarMonth, calendarMonth)
    }
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
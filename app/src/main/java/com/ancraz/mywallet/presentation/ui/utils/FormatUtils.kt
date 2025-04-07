package com.ancraz.mywallet.presentation.ui.utils

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


fun Float.toFormattedString(): String{
    return String.format(Locale.US, "%.2f", this)
}


fun Long.timeToString(): String {
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.US)
    val date = Date(this)
    return dateFormat.format(date)
}


fun String.toFloatValue(): Float {
    return try {
        this.toFloat()
    } catch (e: Exception) {
        debugLog("convert String to Float exception: ${e.message}")
        0f
    }
}

fun getFormattedPeriodLabel(period: AnalyticsPeriod, offset: Int): String {
    val zone = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("dd.MM")

    return when (period) {
        is AnalyticsPeriod.Day -> {
            val date = LocalDate.now().plusDays(offset.toLong())
            date.format(formatter)
        }

        is AnalyticsPeriod.Week -> {
            val startOfWeek = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .plusWeeks(offset.toLong())
            val endOfWeek = startOfWeek.plusDays(6)
            "${startOfWeek.format(formatter)}–${endOfWeek.format(formatter)}"
        }

        is AnalyticsPeriod.Month -> {
            val date = LocalDate.now().withDayOfMonth(1).plusMonths(offset.toLong())
            val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
            "${date.format(formatter)}–${endOfMonth.format(formatter)}"
        }

        is AnalyticsPeriod.Year -> {
            val year = LocalDate.now().plusYears(offset.toLong()).year
            val start = LocalDate.of(year, 1, 1)
            val end = LocalDate.of(year, 12, 31)
            "${start.format(formatter)}–${end.format(formatter)}"
        }

        is AnalyticsPeriod.CustomPeriod -> {
            val from = Instant.ofEpochMilli(period.from).atZone(zone).toLocalDate()
            val to = Instant.ofEpochMilli(period.to).atZone(zone).toLocalDate()
            "${from.format(formatter)}–${to.format(formatter)}"
        }
    }
}

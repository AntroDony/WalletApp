package com.ancraz.mywallet.presentation.models

sealed class AnalyticsPeriod(val name: String) {
    data object Day: AnalyticsPeriod("Day")
    data object Week: AnalyticsPeriod("Week")
    data object Month: AnalyticsPeriod("Month")
    data object Year: AnalyticsPeriod("Year")
    data class CustomPeriod(
        val from: Long,
        val to: Long
    ): AnalyticsPeriod("Custom")
}
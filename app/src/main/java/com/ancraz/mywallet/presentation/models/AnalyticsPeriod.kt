package com.ancraz.mywallet.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AnalyticsPeriod(val name: String): Parcelable {
    data object Day: AnalyticsPeriod("Day")
    data object Week: AnalyticsPeriod("Week")
    data object Month: AnalyticsPeriod("Month")
    data object Year: AnalyticsPeriod("Year")
    data class CustomPeriod(
        val from: Long,
        val to: Long
    ): AnalyticsPeriod("Custom")

}
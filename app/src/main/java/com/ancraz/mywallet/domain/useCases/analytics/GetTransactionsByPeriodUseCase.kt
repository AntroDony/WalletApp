package com.ancraz.mywallet.domain.useCases.analytics

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class GetTransactionsByPeriodUseCase @Inject constructor() {

    operator fun invoke(period: AnalyticsPeriod, transactionList: List<Transaction>, offset: Int): List<Transaction>{
        var periodRange = Pair<Long, Long>(0, System.currentTimeMillis())

        when(period){
            is AnalyticsPeriod.Day -> {
                val day = LocalDate.now().plusDays(offset.toLong())
                debugLog("day: $day | ${offset.toLong()}")
                periodRange = getStartAndEndOfDay(day)
            }
            is AnalyticsPeriod.Week -> {
                periodRange = getStartAndEndOfWeek(offset)
            }

            is AnalyticsPeriod.Month -> {
                periodRange = getStartAndEndOfMonth(offset)
            }

            is AnalyticsPeriod.Year -> {
                periodRange = getStartAndEndOfYear(offset)
            }

            is AnalyticsPeriod.CustomPeriod -> {
                periodRange = (period.from to period.to)
            }
        }

        return transactionList.filter {
           it.time in periodRange.first .. periodRange.second
        }
    }

    private fun getStartAndEndOfDay(date: LocalDate): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val start = date.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        return start to end
    }


    private fun getStartAndEndOfWeek(offset: Int): Pair<Long, Long> {
        val today = LocalDate.now().plusWeeks(offset.toLong())
        val startOfWeek = today.with(DayOfWeek.MONDAY)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endOfWeek = today.with(DayOfWeek.SUNDAY)
            .atTime(LocalTime.MAX) // 23:59:59.999
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return startOfWeek to endOfWeek
    }


    private fun getStartAndEndOfMonth(offset: Int): Pair<Long, Long> {
        val targetMonth = LocalDate.now().plusMonths(offset.toLong())

        val startOfMonth = targetMonth.withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endOfMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth())
            .atTime(LocalTime.MAX) // 23:59:59.999
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return startOfMonth to endOfMonth
    }


    private fun getStartAndEndOfYear(offset: Int): Pair<Long, Long> {
        val targetYear = LocalDate.now().plusYears(offset.toLong())

        val startOfYear = targetYear.withDayOfYear(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endOfYear = targetYear.withDayOfYear(targetYear.lengthOfYear())
            .atTime(LocalTime.MAX)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return startOfYear to endOfYear
    }

}
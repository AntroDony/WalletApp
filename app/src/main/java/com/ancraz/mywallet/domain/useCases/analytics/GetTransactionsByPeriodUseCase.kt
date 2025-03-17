package com.ancraz.mywallet.domain.useCases.analytics

import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetTransactionsByPeriodUseCase @Inject constructor() {

    operator fun invoke(period: AnalyticsPeriod, transactionList: List<Transaction>): List<Transaction>{
        var startTime = 0L
        var endTime = System.currentTimeMillis()

        when(period){
            is AnalyticsPeriod.Day -> {
                startTime = getStartOfToday()
            }
            is AnalyticsPeriod.Week -> {
                startTime = getStartOfWeek()
            }

            is AnalyticsPeriod.Month -> {
                startTime = getStartOfMonth()
            }

            is AnalyticsPeriod.Year -> {
                startTime = getStartOfYear()
            }

            is AnalyticsPeriod.CustomPeriod -> {
                startTime = period.from
                endTime = period.to
            }
        }
        return transactionList.filter {
           it.time in startTime..endTime
        }
    }

    private fun getStartOfToday(): Long{
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun getStartOfWeek(): Long {
        return LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun getStartOfMonth(): Long {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun getStartOfYear(): Long {
        return LocalDate.now().withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
package com.ancraz.mywallet.presentation.ui.screens.analytics

import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionUi

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val data: AnalyticsScreenData = AnalyticsScreenData(),
    val error: String? = null
) {

    data class AnalyticsScreenData(
        val totalBalanceInUsd: Float = 0f,
        val incomeValueInUsd: Float = 0f,
        val expenseValueInUsd: Float = 0f,
        val period: AnalyticsPeriod = AnalyticsPeriod.Day,
        val transactionList: List<TransactionUi> = emptyList(),
        val filteredTransactionList: List<TransactionUi> = emptyList(),
    )
}
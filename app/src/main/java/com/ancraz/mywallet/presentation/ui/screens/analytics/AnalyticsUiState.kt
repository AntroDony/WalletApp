package com.ancraz.mywallet.presentation.ui.screens.analytics

import android.os.Parcelable
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val data: AnalyticsScreenData = AnalyticsScreenData(),
    val error: String? = null
): Parcelable {

    @Parcelize
    data class AnalyticsScreenData(
        val totalBalanceInUsd: String = "$ 0.00",
        val incomeValueInUsd: String = "$ 0.00",
        val expenseValueInUsd: String = "$ 0.00",
        val period: AnalyticsPeriod = AnalyticsPeriod.Day,
        val periodOffset: Int = 0,
        val transactionType: TransactionType? = null,
        val transactionCategory: TransactionCategoryUi? = null,
        val filteredTransactionList: List<TransactionUi> = emptyList(),
        val transactionCategoryList: List<TransactionCategoryUi> = emptyList()
    ): Parcelable


}
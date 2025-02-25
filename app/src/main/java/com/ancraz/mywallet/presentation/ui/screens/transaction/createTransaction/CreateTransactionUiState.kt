package com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction

import com.ancraz.mywallet.presentation.models.CurrencyRateUi
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi

data class CreateTransactionUiState(
    val isLoading: Boolean = false,
    val data: TransactionScreenData = TransactionScreenData(),
    val error: String? = null
){

    data class TransactionScreenData(
        val incomeCategories: List<TransactionCategoryUi> = emptyList(),
        val expenseCategories: List<TransactionCategoryUi> = emptyList(),
        val currencyRates: List<CurrencyRateUi> = emptyList()
    )
}
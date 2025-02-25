package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList

import com.ancraz.mywallet.presentation.models.TransactionUi

data class TransactionListUiState(
    val isLoading: Boolean = false,
    val transactionList: List<TransactionUi> = emptyList(),
    val error: String? = null
)
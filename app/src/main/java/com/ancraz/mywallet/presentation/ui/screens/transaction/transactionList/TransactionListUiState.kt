package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList

import androidx.compose.runtime.Stable
import com.ancraz.mywallet.presentation.models.TransactionUi

@Stable
data class TransactionListUiState(
    val isLoading: Boolean = true,
    val transactionList: List<TransactionUi> = emptyList(),
    val error: String? = null
)
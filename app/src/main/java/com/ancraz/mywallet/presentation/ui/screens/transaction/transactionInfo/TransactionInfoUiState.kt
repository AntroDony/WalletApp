package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo

import com.ancraz.mywallet.presentation.models.TransactionUi

data class TransactionInfoUiState(
    val isLoading: Boolean = true,
    val transaction: TransactionUi? = null,
    val error: String? = null
)
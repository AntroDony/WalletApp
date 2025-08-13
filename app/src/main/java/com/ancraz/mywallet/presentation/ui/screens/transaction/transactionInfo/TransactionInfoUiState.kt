package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo

import androidx.compose.runtime.Stable
import com.ancraz.mywallet.presentation.models.TransactionUi

@Stable
data class TransactionInfoUiState(
    val isLoading: Boolean = true,
    val transaction: TransactionUi? = null,
    val error: String? = null
)
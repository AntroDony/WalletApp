package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.TransactionUi
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class TransactionListUiState(
    val isLoading: Boolean = true,
    val transactionList: List<TransactionUi> = emptyList(),
    val filteredType: TransactionType? = null,
    val error: String? = null
): Parcelable
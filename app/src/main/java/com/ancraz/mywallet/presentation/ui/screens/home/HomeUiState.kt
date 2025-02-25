package com.ancraz.mywallet.presentation.ui.screens.home

import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi

data class HomeUiState(
    val isLoading: Boolean = false,
    val data: HomeScreenData = HomeScreenData(),
    val error: String? = null
){
    data class HomeScreenData(
        val balance: Float = 0f,
        val wallets: List<WalletUi> = emptyList(),
        val transactions: List<TransactionUi> = emptyList()
    )
}



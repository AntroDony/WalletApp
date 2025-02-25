package com.ancraz.mywallet.presentation.ui.screens.wallet.walletList

import com.ancraz.mywallet.presentation.models.WalletUi

data class WalletListUiState(
    val isLoading: Boolean = false,
    val walletList: List<WalletUi> = emptyList(),
    val error: String? = null
)
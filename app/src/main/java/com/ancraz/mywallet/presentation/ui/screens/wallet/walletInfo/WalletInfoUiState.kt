package com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo

import com.ancraz.mywallet.presentation.models.WalletUi

data class WalletInfoUiState(
    val isLoading: Boolean = false,
    val wallet: WalletUi? = null,
    val error: String? = null
)

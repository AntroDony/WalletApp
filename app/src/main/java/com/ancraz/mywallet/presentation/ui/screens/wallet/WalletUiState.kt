package com.ancraz.mywallet.presentation.ui.screens.wallet

import com.ancraz.mywallet.presentation.models.WalletUi

data class WalletUiState(
    val isLoading: Boolean = false,
    val wallet: WalletUi? = null,
    val error: String? = null
)

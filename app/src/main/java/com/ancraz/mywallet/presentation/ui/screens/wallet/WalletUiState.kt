package com.ancraz.mywallet.presentation.ui.screens.wallet

import androidx.compose.runtime.Stable
import com.ancraz.mywallet.presentation.models.WalletUi

@Stable
data class WalletUiState(
    val isLoading: Boolean = true,
    val wallet: WalletUi? = null,
    val error: String? = null
)

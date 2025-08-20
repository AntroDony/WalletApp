package com.ancraz.mywallet.presentation.ui.screens.wallet.walletList

import android.os.Parcelable
import com.ancraz.mywallet.presentation.models.WalletUi
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletListUiState(
    val isLoading: Boolean = true,
    val walletList: List<WalletUi> = emptyList(),
    val error: String? = null
): Parcelable
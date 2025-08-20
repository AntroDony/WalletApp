package com.ancraz.mywallet.presentation.ui.screens.wallet

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.ancraz.mywallet.presentation.models.WalletUi
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class WalletUiState(
    val isLoading: Boolean = true,
    val wallet: WalletUi? = null,
    val error: String? = null
): Parcelable

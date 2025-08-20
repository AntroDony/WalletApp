package com.ancraz.mywallet.presentation.ui.screens.editBalance

import android.os.Parcelable
import com.ancraz.mywallet.core.models.CurrencyCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditBalanceUiState(
    val isLoading: Boolean = true,
    val data: EditBalanceScreenData = EditBalanceScreenData(),
    val error: String? = null
): Parcelable{

    @Parcelize
    data class EditBalanceScreenData(
        val currentTotalBalance: Float = 0f,
        val currency: CurrencyCode = CurrencyCode.USD
    ): Parcelable
}

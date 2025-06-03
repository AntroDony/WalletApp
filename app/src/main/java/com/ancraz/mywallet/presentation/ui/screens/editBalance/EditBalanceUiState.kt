package com.ancraz.mywallet.presentation.ui.screens.editBalance

import com.ancraz.mywallet.core.models.CurrencyCode


data class EditBalanceUiState(
    val isLoading: Boolean = true,
    val data: EditBalanceScreenData = EditBalanceScreenData(),
    val error: String? = null
){

    data class EditBalanceScreenData(
        val currentTotalBalance: Float = 0f,
        val currency: CurrencyCode = CurrencyCode.USD
    )
}

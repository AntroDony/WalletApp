package com.ancraz.mywallet.presentation.states

import com.ancraz.mywallet.presentation.models.CurrencyRateUi

data class CurrencyRateState(
    val isLoading: Boolean = false,
    val rates: List<CurrencyRateUi> = emptyList(),
    val error: String? = null
)
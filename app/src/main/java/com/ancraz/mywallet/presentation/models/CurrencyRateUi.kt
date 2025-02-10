package com.ancraz.mywallet.presentation.models

import com.ancraz.mywallet.core.models.CurrencyCode

data class CurrencyRateUi(
    val currencyCode: CurrencyCode,
    val rate: Float
)
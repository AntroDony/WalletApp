package com.ancraz.mywallet.domain.models

import com.ancraz.mywallet.core.models.CurrencyCode

data class CurrencyRate(

    val currencyCode: CurrencyCode,

    val rateValue: Float,
)
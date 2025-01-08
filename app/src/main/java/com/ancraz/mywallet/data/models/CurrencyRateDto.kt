package com.ancraz.mywallet.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRateDto(

    val currencyCode: String,

    val rate: Float,
)

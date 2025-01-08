package com.ancraz.mywallet.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto(

    @SerialName("date")
    val rateDate: String,

    @SerialName("base")
    val base: String,

    @SerialName("rates")
    val rates: List<CurrencyRateDto>
)
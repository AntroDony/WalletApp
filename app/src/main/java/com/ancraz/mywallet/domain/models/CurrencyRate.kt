package com.ancraz.mywallet.domain.models

data class CurrencyRate(

    val updateDate: Long,

    val currencyCode: CurrencyCode,

    val rateValue: Float,
)

enum class CurrencyCode {
    USD, EUR, RUB, GEL, KZT, UNKNOWN
}
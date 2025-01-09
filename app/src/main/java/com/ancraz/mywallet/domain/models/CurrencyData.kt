package com.ancraz.mywallet.domain.models

data class CurrencyData(

    val updateTime: Long? = null,

    val rates: List<CurrencyRate> = emptyList()
)

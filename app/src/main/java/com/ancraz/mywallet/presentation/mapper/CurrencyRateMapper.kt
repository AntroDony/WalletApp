package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.presentation.models.CurrencyRateUi

fun CurrencyRate.toCurrencyRateUi(): CurrencyRateUi {
    return CurrencyRateUi(
        currencyCode = this.currencyCode,
        rate = this.rateValue
    )
}


fun CurrencyRateUi.toCurrencyRate(): CurrencyRate {
    return CurrencyRate(
        currencyCode = this.currencyCode,
        rateValue = this.rate
    )
}
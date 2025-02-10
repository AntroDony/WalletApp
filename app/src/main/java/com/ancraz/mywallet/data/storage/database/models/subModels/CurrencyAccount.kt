package com.ancraz.mywallet.data.storage.database.models.subModels

import com.ancraz.mywallet.core.models.CurrencyCode

data class CurrencyAccount(
    val currencyCode: CurrencyCode,
    val value: Float
)
package com.ancraz.mywallet.domain.repository

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.domain.models.CurrencyData

interface CurrencyRepository {

    suspend fun getCurrenciesRate(
        desiredCurrencies: List<String>,
        baseCurrencyCode: String = "USD"
    ): Result<CurrencyData, NetworkError>
}
package com.ancraz.mywallet.domain.network

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.domain.models.CurrencyData

interface NetworkDataSource {

    suspend fun getDesiredCurrenciesRate(
        desiredCurrenciesString: String,
        baseCurrencyCode: String
    ): Result<CurrencyData, NetworkError>
}
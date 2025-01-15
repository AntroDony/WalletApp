package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.domain.models.CurrencyData
import com.ancraz.mywallet.domain.network.CurrencyDataSource
import com.ancraz.mywallet.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val networkDataSource: CurrencyDataSource,
): CurrencyRepository {

    override suspend fun getCurrenciesRate(
        desiredCurrencies: List<String>,
        baseCurrencyCode: String
    ): Result<CurrencyData, NetworkError> {
        return networkDataSource.getDesiredCurrenciesRate(desiredCurrencies.joinToString(separator = ","), baseCurrencyCode)
    }
}
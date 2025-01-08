package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.domain.network.NetworkDataSource
import com.ancraz.mywallet.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
): CurrencyRepository {

    override suspend fun getCurrenciesRate(
        desiredCurrencies: List<String>,
        baseCurrencyCode: String
    ): Result<List<CurrencyRate>, NetworkError> {
        return networkDataSource.getDesiredCurrenciesRate(desiredCurrencies.joinToString(separator = ","), baseCurrencyCode)
    }
}
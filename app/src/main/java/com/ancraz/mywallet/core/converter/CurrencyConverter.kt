package com.ancraz.mywallet.core.converter

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import javax.inject.Inject

class CurrencyConverter(
    private val dataStore: DataStoreRepository
){

    suspend fun convertToUsd(value: Float, currencyCode: CurrencyCode): Float{
        val currentRate = when(currencyCode){
            CurrencyCode.EUR -> dataStore.getCurrencyRateToUsd(CurrencyCode.EUR)
            CurrencyCode.RUB -> dataStore.getCurrencyRateToUsd(CurrencyCode.RUB)
            CurrencyCode.GEL -> dataStore.getCurrencyRateToUsd(CurrencyCode.GEL)
            CurrencyCode.KZT -> dataStore.getCurrencyRateToUsd(CurrencyCode.KZT)
            CurrencyCode.USD -> 1f
        }

        return value / currentRate
    }
}
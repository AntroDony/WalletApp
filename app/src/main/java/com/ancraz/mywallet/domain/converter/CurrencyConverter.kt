package com.ancraz.mywallet.domain.converter

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository

class CurrencyConverter(
    private val dataStore: DataStoreRepository
){

    suspend fun convertToUsd(value: Float, currencyCode: CurrencyCode): Float?{
        val currentRate = when(currencyCode){
            CurrencyCode.EUR -> dataStore.getCurrencyRateToUsd(CurrencyCode.EUR)
            CurrencyCode.RUB -> dataStore.getCurrencyRateToUsd(CurrencyCode.RUB)
            CurrencyCode.GEL -> dataStore.getCurrencyRateToUsd(CurrencyCode.GEL)
            CurrencyCode.KZT -> dataStore.getCurrencyRateToUsd(CurrencyCode.KZT)
            CurrencyCode.USD -> 1f
        }
        return currentRate?.let {
            value / currentRate
        }
    }
}
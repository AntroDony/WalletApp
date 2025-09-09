package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.domain.converter.CurrencyConverter
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import javax.inject.Inject

class UpdateTotalBalanceUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    private val currencyConverter = CurrencyConverter(dataStoreRepository)

    suspend operator fun invoke(value: Float, currency: CurrencyCode){
        when(currency){
            CurrencyCode.USD -> {
                updateBalance(value)
            }
            CurrencyCode.EUR -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.EUR)
                updateBalance(balanceInUsd)
            }
            CurrencyCode.RUB -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.RUB)
                updateBalance(balanceInUsd)
            }
            CurrencyCode.GEL -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.GEL)
                updateBalance(balanceInUsd)
            }
            CurrencyCode.KZT -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.KZT)
                updateBalance(balanceInUsd)
            }
        }
    }


    private suspend fun updateBalance(value: Float?){
        value?.let {
            dataStoreRepository.updateTotalBalanceInUsd(it)
        }
    }
}
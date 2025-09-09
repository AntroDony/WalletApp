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
                balanceInUsd?.let {
                    updateBalance(it)
                }
            }
            CurrencyCode.RUB -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.RUB)
                balanceInUsd?.let {
                    updateBalance(it)
                }
            }
            CurrencyCode.GEL -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.GEL)
                balanceInUsd?.let {
                    updateBalance(it)
                }
            }
            CurrencyCode.KZT -> {
                val balanceInUsd = currencyConverter.convertToUsd(value, CurrencyCode.KZT)
                balanceInUsd?.let{
                    updateBalance(it)
                }
            }
        }
    }


    private suspend fun updateBalance(value: Float){
        dataStoreRepository.updateTotalBalanceInUsd(value)
    }
}
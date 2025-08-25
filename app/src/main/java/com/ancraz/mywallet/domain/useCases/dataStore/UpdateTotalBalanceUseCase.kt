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
                val balanceInRub = currencyConverter.convertToUsd(value, CurrencyCode.RUB)
                balanceInRub?.let {
                    updateBalance(it)
                }
            }
            CurrencyCode.GEL -> {
                val balanceInGel = currencyConverter.convertToUsd(value, CurrencyCode.GEL)
                balanceInGel?.let {
                    updateBalance(it)
                }
            }
            CurrencyCode.KZT -> {
                val balanceInKzt = currencyConverter.convertToUsd(value, CurrencyCode.KZT)
                balanceInKzt?.let{
                    updateBalance(it)
                }
            }
        }
    }


    private suspend fun updateBalance(value: Float){
        dataStoreRepository.updateTotalBalanceInUsd(value)
    }
}
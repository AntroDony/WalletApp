package com.ancraz.mywallet.domain.useCases

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TotalBalanceUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    fun getTotalBalanceFlow(): Flow<DataResult<Float>>{
        return flow {
            emit(DataResult.Loading())
            dataStoreRepository.totalBalanceInUsdFlow().collect{ balance->
                emit(DataResult.Success(balance))
            }
        }
    }

    suspend fun editTotalBalance(value: Float, currency: CurrencyCode){
        when(currency){
            CurrencyCode.USD -> {
                updateBalance(value)
            }
            CurrencyCode.EUR -> {
                val balanceInUsd = convertToUsd(value, CurrencyCode.EUR)
                updateBalance(balanceInUsd)
            }
            CurrencyCode.RUB -> {
                val balanceInRub = convertToUsd(value, CurrencyCode.RUB)
                updateBalance(balanceInRub)
            }
            CurrencyCode.GEL -> {
                val balanceInGel = convertToUsd(value, CurrencyCode.GEL)
                updateBalance(balanceInGel)
            }
            CurrencyCode.KZT -> {
                val balanceInKzt = convertToUsd(value, CurrencyCode.KZT)
                updateBalance(balanceInKzt)
            }
        }
    }


    private suspend fun updateBalance(value: Float?){
        value?.let {
            dataStoreRepository.updateTotalBalanceInUsd(value)
        } ?: kotlin.run {
            debugLog("cannot update total balance")
        }
    }


    private suspend fun convertToUsd(value: Float, currency: CurrencyCode): Float {
        val rateInUsd = dataStoreRepository.getCurrencyRateToUsd(currency)
        return value * rateInUsd
    }
}
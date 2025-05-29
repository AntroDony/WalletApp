package com.ancraz.mywallet.domain.useCases.currency

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.core.utils.onError
import com.ancraz.mywallet.core.utils.onSuccess
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.domain.repository.CurrencyRepository
import com.ancraz.mywallet.domain.utils.desiredCurrencies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val remoteRepository: CurrencyRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke(): Flow<List<CurrencyRate>> {
        return flow {
            val currencyLastUpdateTime = dataStoreRepository.getCurrencyLastUpdateTime()
            if (currencyLastUpdateTime == null || needUpdateCurrencyRates(currencyLastUpdateTime)) {
                getCurrencyRatesFromApi { onError: DataResult<List<CurrencyRate>> ->
                    debugLog("getCurrencyRatesFromApi error: ${onError.errorMessage}")
                    emit(emptyList())
                }
            }

            try {
                val resultList = mutableListOf<CurrencyRate>()
                CurrencyCode.entries.forEach { code ->
                    val rate = dataStoreRepository.getCurrencyRateToUsd(code)
                    resultList.add(
                        CurrencyRate(
                            currencyCode = code,
                            rateValue = rate
                        )
                    )

                }

                emit(resultList)
            } catch (e: Exception) {
                debugLog("GetCurrencyRatesUseCase exception: ${e.message}")
            }
        }
    }

    private suspend fun <T> getCurrencyRatesFromApi(onResponseError: suspend (DataResult<T>) -> Unit) {
        debugLog("getCurrencyRatesFromApi()")
        remoteRepository.getCurrenciesRate(desiredCurrencies)
            .onSuccess { currencyData ->
                debugLog("update data in dataStore")
                dataStoreRepository.setLastCurrencyUpdatedTime(
                    currencyData.updateTime ?: Calendar.getInstance().timeInMillis
                )
                currencyData.rates.forEach { rate ->
                    dataStoreRepository.setCurrencyRate(rate)
                }
            }
            .onError { error ->
                onResponseError(DataResult.Error(error.name))
            }
    }


    private fun needUpdateCurrencyRates(lastUpdateTime: Long): Boolean {
        return (Calendar.getInstance().timeInMillis - lastUpdateTime - 24 * 60 * 60 * 1000) > 0
    }
}
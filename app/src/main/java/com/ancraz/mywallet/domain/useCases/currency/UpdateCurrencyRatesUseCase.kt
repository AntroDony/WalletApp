package com.ancraz.mywallet.domain.useCases.currency

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.core.utils.onError
import com.ancraz.mywallet.core.utils.onSuccess
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.CurrencyData
import com.ancraz.mywallet.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class UpdateCurrencyRatesUseCase @Inject constructor(
    private val remoteRepository: CurrencyRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke(): Flow<DataResult<String?>> {
        return flow {
            val currencyLastUpdateTime = dataStoreRepository.getCurrencyLastUpdateTime()

            if (currencyLastUpdateTime == null || needUpdateCurrencyRates(currencyLastUpdateTime)){
                getCurrencyRatesFromApi(
                    onResponseSuccess = { currencyData ->
                        debugLog("update currency in dataStore")
                        dataStoreRepository.setLastCurrencyUpdatedTime(
                            currencyData.updateTime ?: Calendar.getInstance().timeInMillis
                        )
                        currencyData.rates.forEach { rate ->
                            dataStoreRepository.setCurrencyRate(rate)
                        }
                        emit(DataResult.Success(null))

                    },
                    onResponseError = { networkError ->
                        emit(DataResult.Error(networkError.name))
                    }
                )
            }
        }
    }

    private suspend fun getCurrencyRatesFromApi(
        onResponseSuccess: suspend (CurrencyData) -> Unit,
        onResponseError: suspend (NetworkError) -> Unit
    ) {
        debugLog("getCurrencyRatesFromApi()")
        remoteRepository.getCurrenciesRate(
            CurrencyCode.entries.map { it.name }
        )
            .onSuccess { currencyData ->
                onResponseSuccess(currencyData)
            }
            .onError { error ->
                onResponseError(error)
            }
    }

    private fun needUpdateCurrencyRates(lastUpdateTime: Long): Boolean {
        return (Calendar.getInstance().timeInMillis - lastUpdateTime - 24 * 60 * 60 * 1000) > 0
    }
}
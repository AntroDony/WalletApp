package com.ancraz.mywallet.domain.useCases.currency

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    @Suppress("UNCHECKED_CAST")
    operator fun invoke(): Flow<List<CurrencyRate>?> {
        return flow {
            try {
                val resultList = mutableListOf<CurrencyRate>()
                CurrencyCode.entries.forEach { code ->
                    val rate = dataStoreRepository.getCurrencyRateToUsd(code)
                    if (rate != null){
                        resultList.add(
                            CurrencyRate(
                                currencyCode = code,
                                rateValue = rate
                            )
                        )
                    }
                }
                if (resultList.size > 1) {
                    emit(resultList)
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                debugLog("GetCurrencyRatesUseCase exception: ${e.message}")
                emit(null)
            }
        }
    }
}
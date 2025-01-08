package com.ancraz.mywallet.domain.useCases

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.onError
import com.ancraz.mywallet.core.utils.onSuccess
import com.ancraz.mywallet.data.storage.DataStoreRepository
import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.domain.repository.CurrencyRepository
import com.ancraz.mywallet.domain.utils.desiredCurrencies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val remoteRepository: CurrencyRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(): Flow<DataResult<List<CurrencyRate>>>{
        return flow {
            try {
                emit(DataResult.Loading())

                remoteRepository.getCurrenciesRate(desiredCurrencies)
                    .onSuccess { currencyList ->
                        emit(
                            DataResult.Success(currencyList)
                        )
                    }
                    .onError { error ->
                        emit(DataResult.Error(error.name))
                    }
            }
            catch (e: Exception){
                emit(DataResult.Error(e.localizedMessage ?: "GetCurrencyRatesUseCase Exception"))
            }
        }
    }
}
package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTotalBalanceUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke(): Flow<DataResult<Float>>{
        return flow {
            dataStoreRepository.totalBalanceInUsdFlow().collect{ balance->
                emit(DataResult.Success(balance))
            }
        }
    }
}
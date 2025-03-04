package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCurrencyUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke(): Flow<String>{
        return dataStoreRepository.getRecentCurrencyNameFlow()
    }
}
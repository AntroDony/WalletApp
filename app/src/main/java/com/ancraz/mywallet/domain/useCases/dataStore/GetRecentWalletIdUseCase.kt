package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentWalletIdUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke(): Flow<Long?>{
        return dataStoreRepository.getRecentWalletIdFlow()
    }
}
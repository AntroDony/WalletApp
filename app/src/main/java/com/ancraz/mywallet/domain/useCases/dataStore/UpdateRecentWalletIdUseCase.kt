package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import javax.inject.Inject

class UpdateRecentWalletIdUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(walletId: Long){
        dataStoreRepository.setRecentWalletId(walletId)
    }
}
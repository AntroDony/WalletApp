package com.ancraz.mywallet.domain.useCases.dataStore

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import javax.inject.Inject

class UpdateRecentCurrencyUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(currencyName: String){
        dataStoreRepository.setRecentCurrencyName(currencyName)
    }

}
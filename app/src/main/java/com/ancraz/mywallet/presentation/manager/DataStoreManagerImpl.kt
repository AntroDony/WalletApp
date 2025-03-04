package com.ancraz.mywallet.presentation.manager

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.useCases.dataStore.GetRecentCurrencyUseCase
import com.ancraz.mywallet.domain.useCases.dataStore.GetRecentWalletIdUseCase
import com.ancraz.mywallet.domain.useCases.dataStore.GetTotalBalanceUseCase
import com.ancraz.mywallet.domain.useCases.dataStore.UpdateRecentCurrencyUseCase
import com.ancraz.mywallet.domain.useCases.dataStore.UpdateRecentWalletIdUseCase
import com.ancraz.mywallet.domain.useCases.dataStore.UpdateTotalBalanceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManagerImpl @Inject constructor(
    private val getTotalBalanceUseCase: GetTotalBalanceUseCase,
    private val updateTotalBalanceUseCase: UpdateTotalBalanceUseCase,
    private val getRecentWalletIdUseCase: GetRecentWalletIdUseCase,
    private val getRecentCurrencyUseCase: GetRecentCurrencyUseCase,
    private val updateRecentWalletIdUseCase: UpdateRecentWalletIdUseCase,
    private val updateRecentCurrencyUseCase: UpdateRecentCurrencyUseCase
): DataStoreManager {

    override fun getTotalBalance(): Flow<DataResult<Float>> {
        return getTotalBalanceUseCase()
    }

    override fun getRecentWalletId(): Flow<Long?> {
        return getRecentWalletIdUseCase()
    }

    override fun getRecentCurrency(): Flow<CurrencyCode> {
        return getRecentCurrencyUseCase().map { currencyName ->
            when(currencyName){
                CurrencyCode.EUR.name -> CurrencyCode.EUR
                CurrencyCode.RUB.name -> CurrencyCode.RUB
                CurrencyCode.GEL.name -> CurrencyCode.GEL
                CurrencyCode.KZT.name -> CurrencyCode.KZT
                else -> CurrencyCode.USD
            }
        }
    }

    override suspend fun editTotalBalance(newValue: Float, currencyCode: CurrencyCode) {
        updateTotalBalanceUseCase(newValue, currencyCode)
    }

    override suspend fun updateRecentWalletId(id: Long) {
        updateRecentWalletIdUseCase(id)
    }

    override suspend fun updateRecentCurrency(currencyCode: CurrencyCode) {
        updateRecentCurrencyUseCase(currencyCode.name)
    }
}
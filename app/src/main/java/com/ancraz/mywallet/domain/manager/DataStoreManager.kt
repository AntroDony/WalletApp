package com.ancraz.mywallet.domain.manager

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    fun getTotalBalance(): Flow<Float>

    fun getRecentWalletId(): Flow<Long>

    fun getRecentCurrency(): Flow<CurrencyCode>

    fun getPrivateModeStatus(): Flow<Boolean>

    suspend fun editTotalBalance(newValue: Float, currencyCode: CurrencyCode)

    suspend fun updateRecentWalletId(id: Long)

    suspend fun updateRecentCurrency(currencyCode: CurrencyCode)

    suspend fun updatePrivateModeStatus(isPrivate: Boolean)
}
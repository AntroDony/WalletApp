package com.ancraz.mywallet.domain.manager

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.models.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletManager {

    fun getWallets(): Flow<List<Wallet>>

    suspend fun getWalletById(id: Long): DataResult<Wallet>

    suspend fun addWallet(wallet: Wallet)

    suspend fun updateWallet(wallet: Wallet)

    suspend fun deleteWalletById(id: Long)
}
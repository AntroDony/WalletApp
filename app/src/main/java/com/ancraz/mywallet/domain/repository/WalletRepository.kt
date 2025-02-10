package com.ancraz.mywallet.domain.repository

import com.ancraz.mywallet.domain.models.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    fun getWalletList(): Flow<List<Wallet>>

    suspend fun getWalletById(id: Long): Wallet

    suspend fun addWallet(wallet: Wallet)

    suspend fun updateWallet(wallet: Wallet)

    suspend fun deleteWalletById(id: Long)
}
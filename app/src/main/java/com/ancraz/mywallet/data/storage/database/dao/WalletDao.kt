package com.ancraz.mywallet.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ancraz.mywallet.data.storage.database.models.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM wallets")
    fun getAllWallets(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE wallet_id = :id")
    suspend fun getWalletById(id: Long): WalletEntity

    @Insert
    suspend fun addNewWallet(wallet: WalletEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWallet(wallet: WalletEntity)

    @Query("DELETE FROM wallets WHERE wallet_id = :id")
    suspend fun deleteWalletById(id: Long)
}
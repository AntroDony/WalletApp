package com.ancraz.mywallet.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ancraz.mywallet.data.storage.database.models.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>


//    @Query("SELECT * FROM transactions WHERE account_id = :accountId")
//    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>


    @Query("SELECT * FROM transactions WHERE transaction_id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity


    @Query("DELETE FROM transactions WHERE transaction_id = :id")
    fun deleteTransactionById(id: Long)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: TransactionEntity)


    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

}
package com.ancraz.mywallet.domain.manager

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionManager {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): DataResult<Transaction>

    suspend fun addTransaction(transaction: Transaction)

    suspend fun deleteTransaction(id: Long)
}
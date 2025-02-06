package com.ancraz.mywallet.domain.repository

import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactionList(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Transaction

    suspend fun addNewTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun deleteTransactionById(id: Long)


    fun getIncomeCategoryList(): Flow<List<TransactionCategory>>

    fun getExpenseCategoryList(): Flow<List<TransactionCategory>>

    suspend fun addNewIncomeCategory(category: TransactionCategory)

    suspend fun addNewExpenseCategory(category: TransactionCategory)

    suspend fun deleteCategoryById(id: Long)

}
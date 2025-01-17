package com.ancraz.mywallet.domain.repository

import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory
import com.ancraz.mywallet.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactionList(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Transaction

    suspend fun addNewTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun deleteTransactionById(id: Long)


    fun getIncomeCategoryList(): Flow<List<IncomeTransactionCategory>>

    fun getExpenseCategoryList(): Flow<List<ExpenseTransactionCategory>>

    suspend fun addNewIncomeCategory(category: IncomeTransactionCategory)

    suspend fun addNewExpenseCategory(category: ExpenseTransactionCategory)

    suspend fun deleteIncomeCategoryById(id: Long)

    suspend fun deleteExpenseCategoryById(id: Long)

}
package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.mappers.toCategoryEntity
import com.ancraz.mywallet.data.mappers.toTransaction
import com.ancraz.mywallet.data.mappers.toTransactionCategory
import com.ancraz.mywallet.data.mappers.toTransactionEntity
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
): TransactionRepository {

    override fun getTransactionList(): Flow<List<Transaction>>{
        return transactionDao.getAllTransactions().map { list ->
            list.map { transaction ->
                transaction.toTransaction()
            }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction {
        return transactionDao.getTransactionById(id).toTransaction()
    }

    override suspend fun addNewTransaction(transaction: Transaction){
        transactionDao.insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }


    override fun getExpenseCategoryList(): Flow<List<ExpenseTransactionCategory>> {
        return categoryDao.getAllExpenseCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override fun getIncomeCategoryList(): Flow<List<IncomeTransactionCategory>> {
        return categoryDao.getAllIncomeCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override suspend fun addNewExpenseCategory(category: ExpenseTransactionCategory) {
        categoryDao.insertExpenseCategory(category.toCategoryEntity())
    }


    override suspend fun addNewIncomeCategory(category: IncomeTransactionCategory) {
        categoryDao.insertIncomeCategory(category.toCategoryEntity())
    }


    override suspend fun deleteExpenseCategoryById(id: Long) {
        categoryDao.deleteExpenseCategoryById(id)
    }


    override suspend fun deleteIncomeCategoryById(id: Long) {
        categoryDao.deleteIncomeCategoryById(id)
    }
}
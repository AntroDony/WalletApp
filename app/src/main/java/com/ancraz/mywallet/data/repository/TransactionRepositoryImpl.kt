package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.data.mappers.toCategoryEntity
import com.ancraz.mywallet.data.mappers.toTransaction
import com.ancraz.mywallet.data.mappers.toTransactionCategory
import com.ancraz.mywallet.data.mappers.toTransactionEntity
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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


    override fun getExpenseCategoryList(): Flow<List<TransactionCategory>> {
        return categoryDao.getAllExpenseCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override fun getIncomeCategoryList(): Flow<List<TransactionCategory>> {
        return categoryDao.getAllIncomeCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override suspend fun addNewExpenseCategory(category: TransactionCategory) {
        categoryDao.insertCategory(category.toCategoryEntity())
    }


    override suspend fun addNewIncomeCategory(category: TransactionCategory) {
        categoryDao.insertCategory(category.toCategoryEntity())
    }


    override suspend fun deleteCategoryById(id: Long) {
        categoryDao.deleteCategoryById(id)
    }

}
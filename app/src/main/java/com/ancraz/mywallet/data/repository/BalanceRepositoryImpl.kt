package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.data.mappers.toTransaction
import com.ancraz.mywallet.data.mappers.toTransactionEntity
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.BalanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BalanceRepositoryImpl(
    private val transactionDao: TransactionDao
): BalanceRepository {

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
}
package com.ancraz.mywallet.presentation.manager

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.manager.TransactionManager
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.useCases.transaction.AddTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transaction.DeleteTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.transaction.GetTransactionByIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionManagerImpl @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
): TransactionManager {

    override fun getTransactions(): Flow<List<Transaction>> {
        return getAllTransactionsUseCase()
    }

    override suspend fun getTransactionById(id: Long): DataResult<Transaction> {
        return getTransactionByIdUseCase(id)
    }

    override suspend fun addTransaction(transaction: Transaction) {
        addTransactionUseCase(transaction)
    }

    override suspend fun deleteTransaction(id: Long) {
        deleteTransactionUseCase(id)
    }
}
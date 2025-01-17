package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    suspend fun addIncomeTransaction(transaction: Transaction){
        transactionRepository.addNewTransaction(transaction)

        dataStoreRepository.incomeTotalBalance(transaction.value)
    }


    suspend fun addExpenseTransaction(transaction: Transaction){
        transactionRepository.addNewTransaction(transaction)

        dataStoreRepository.expenseTotalBalance(transaction.value)
    }
}
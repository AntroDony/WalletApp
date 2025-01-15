package com.ancraz.mywallet.domain.useCases

import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.BalanceRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    suspend fun addIncomeTransaction(transaction: Transaction){
        balanceRepository.addNewTransaction(transaction)

        dataStoreRepository.incomeTotalBalance(transaction.value)
    }


    suspend fun addExpenseTransaction(transaction: Transaction){
        balanceRepository.addNewTransaction(transaction)

        dataStoreRepository.expenseTotalBalance(transaction.value)
    }
}
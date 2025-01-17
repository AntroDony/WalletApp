package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun deleteIncomeCategory(id: Long){
        transactionRepository.deleteIncomeCategoryById(id)
    }

    suspend fun deleteExpenseCategory(id: Long){
        transactionRepository.deleteExpenseCategoryById(id)
    }
}
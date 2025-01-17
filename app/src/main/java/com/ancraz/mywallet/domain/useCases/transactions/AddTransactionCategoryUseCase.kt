package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun addIncomeCategory(category: IncomeTransactionCategory){
        transactionRepository.addNewIncomeCategory(category)
    }

    suspend fun addExpenseCategory(category: ExpenseTransactionCategory){
        transactionRepository.addNewExpenseCategory(category)
    }
}
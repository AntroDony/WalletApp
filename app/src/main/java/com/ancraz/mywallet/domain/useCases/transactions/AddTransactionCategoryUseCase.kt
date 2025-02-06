package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(category: TransactionCategory){
        transactionRepository.addNewExpenseCategory(category)
    }
}
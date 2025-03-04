package com.ancraz.mywallet.domain.useCases.transactionCategory

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(category: TransactionCategory){
        when(category.categoryType){
            TransactionType.INCOME -> {
                transactionRepository.addNewIncomeCategory(category)
            }

            TransactionType.EXPENSE -> {
                transactionRepository.addNewExpenseCategory(category)
            }

            TransactionType.TRANSFER -> {
                //to categories for this type
            }
        }
    }
}
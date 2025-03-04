package com.ancraz.mywallet.domain.useCases.transactionCategory

import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(id: Long){
        transactionRepository.deleteCategoryById(id)
    }
}
package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(id: Long){
        transactionRepository.deleteCategoryById(id)
    }
}
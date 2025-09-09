package com.ancraz.mywallet.domain.useCases.transaction

import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
){

    suspend operator fun invoke(id: Long){
        transactionRepository.deleteTransactionById(id)
    }
}
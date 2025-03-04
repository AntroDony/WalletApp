package com.ancraz.mywallet.domain.useCases.transaction

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): DataResult<Transaction> {
        return try {
            transactionRepository.getTransactionById(id).let { transaction ->
                debugLog("GetTransactionByIdUseCase : $transaction")
                DataResult.Success(transaction)
            }
        } catch (e: Exception) {
            debugLog("getTransactions exception: ${e.message}")
            DataResult.Error("${e.message}")
        }
    }
}
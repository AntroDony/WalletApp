package com.ancraz.mywallet.domain.useCases.transaction

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(): Flow<List<Transaction>> {
        return try {
            transactionRepository.getTransactionList().map { transactions ->
                transactions.reversed()
            }
        } catch (e: Exception) {
            debugLog("getTransactions exception: ${e.message}")
            flowOf(emptyList())
        }
    }
}
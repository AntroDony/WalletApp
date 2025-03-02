package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
){

    operator fun invoke(): Flow<DataResult<List<Transaction>>> {
        return flow {
            try {
                emit(DataResult.Loading())

                transactionRepository.getTransactionList().collect{ transactions ->
                    emit(DataResult.Success(transactions))
                }
            }
            catch (e: Exception){
                debugLog("getTransactions exception: ${e.message}")
                emit(DataResult.Error("${e.message}"))
            }
        }
    }
}
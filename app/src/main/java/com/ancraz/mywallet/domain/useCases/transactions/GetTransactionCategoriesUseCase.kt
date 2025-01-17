package com.ancraz.mywallet.domain.useCases.transactions

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTransactionCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    fun getIncomeCategories(): Flow<DataResult<List<IncomeTransactionCategory>>>{
        return flow {
           try {
               emit(DataResult.Loading())

               transactionRepository.getIncomeCategoryList().collect{ categories ->
                   emit(DataResult.Success(categories))
               }
           }
           catch (e: Exception){
               debugLog("getIncomeCategories exception: ${e.message}")
               emit(DataResult.Error("${e.message}"))
           }
        }
    }


    fun getExpenseCategories(): Flow<DataResult<List<ExpenseTransactionCategory>>>{
        return flow {
            try {
                emit(DataResult.Loading())

                transactionRepository.getExpenseCategoryList().collect{ categories ->
                    emit(DataResult.Success(categories))
                }
            }
            catch (e: Exception){
                debugLog("getExpenseCategories exception: ${e.message}")
                emit(DataResult.Error("${e.message}"))
            }
        }
    }
}
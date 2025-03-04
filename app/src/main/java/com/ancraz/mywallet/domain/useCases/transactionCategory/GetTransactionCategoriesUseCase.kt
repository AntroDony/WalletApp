package com.ancraz.mywallet.domain.useCases.transactionCategory

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTransactionCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(transactionType: TransactionType): Flow<DataResult<List<TransactionCategory>>>{
        return flow {
            try {
                emit(DataResult.Loading())

                when(transactionType){
                    TransactionType.INCOME -> {
                        transactionRepository.getIncomeCategoryList().collect{ categories ->
                            emit(DataResult.Success(categories))
                        }
                    }

                    TransactionType.EXPENSE -> {
                        transactionRepository.getExpenseCategoryList().collect{ categories ->
                            emit(DataResult.Success(categories))
                        }
                    }

                    TransactionType.TRANSFER -> {
                        //no categories for this type
                    }
                }

            }
            catch (e: Exception){
                debugLog("getTransactionCategories exception: ${e.message}")
                emit(DataResult.Error("${e.message}"))
            }
        }
    }
}
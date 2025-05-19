package com.ancraz.mywallet.domain.useCases.transactionCategory

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTransactionCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(transactionType: TransactionType? = null): Flow<DataResult<List<TransactionCategory>>>{
        return channelFlow {
            try {
                send(DataResult.Loading())

                when(transactionType){
                    TransactionType.INCOME -> {
                        transactionRepository.getIncomeCategoryList().collect{ categories ->
                            send(DataResult.Success(categories))
                        }
                    }

                    TransactionType.EXPENSE -> {
                        transactionRepository.getExpenseCategoryList().collect{ categories ->
                            send(DataResult.Success(categories))
                        }
                    }

                    TransactionType.TRANSFER -> {
                        //no categories for this type
                    }

                    null -> {
                        combine(
                            transactionRepository.getExpenseCategoryList(),
                            transactionRepository.getIncomeCategoryList()
                        ){ expenseCategories, incomeCategories ->
                            expenseCategories + incomeCategories
                        }.collectLatest {
                            send(DataResult.Success(it.distinct()))
                        }
                    }
                }

            }
            catch (e: Exception){
                debugLog("getTransactionCategories exception: ${e.message}")
                send(DataResult.Error("${e.message}"))
            }
        }
    }
}
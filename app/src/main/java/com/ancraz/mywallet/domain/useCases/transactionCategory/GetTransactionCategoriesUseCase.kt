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

    operator fun invoke(transactionType: TransactionType? = null): Flow<List<TransactionCategory>>{
        return channelFlow {
            try {
                when(transactionType){
                    TransactionType.INCOME -> {
                        transactionRepository.getIncomeCategoryList().collect{ categories ->
                            send(categories)
                        }
                    }

                    TransactionType.EXPENSE -> {
                        transactionRepository.getExpenseCategoryList().collect{ categories ->
                            send(categories)
                        }
                    }

                    TransactionType.TRANSFER -> {
                        send(emptyList())
                    }

                    null -> {
                        combine(
                            transactionRepository.getExpenseCategoryList(),
                            transactionRepository.getIncomeCategoryList()
                        ){ expenseCategories, incomeCategories ->
                            expenseCategories + incomeCategories
                        }.collectLatest {
                            send(it.distinct())
                        }
                    }
                }

            }
            catch (e: Exception){
                debugLog("getTransactionCategories exception: ${e.message}")
            }
        }
    }
}
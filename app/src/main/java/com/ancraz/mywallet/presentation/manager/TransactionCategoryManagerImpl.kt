package com.ancraz.mywallet.presentation.manager

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.manager.TransactionCategoryManager
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.useCases.transactionCategory.AddTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactionCategory.DeleteTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactionCategory.GetTransactionCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionCategoryManagerImpl @Inject constructor(
    private val getTransactionCategoriesUseCase: GetTransactionCategoriesUseCase,
    private val addTransactionCategoryUseCase: AddTransactionCategoryUseCase,
    private val deleteTransactionCategoryUseCase: DeleteTransactionCategoryUseCase
): TransactionCategoryManager  {

    override fun getCategories(transactionType: TransactionType): Flow<DataResult<List<TransactionCategory>>> {
        return getTransactionCategoriesUseCase(transactionType)
    }

    override suspend fun addCategory(category: TransactionCategory) {
        addTransactionCategoryUseCase(category)
    }

    override suspend fun deleteCategory(id: Long) {
        deleteTransactionCategoryUseCase(id)
    }
}
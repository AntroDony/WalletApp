package com.ancraz.mywallet.domain.manager

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.models.TransactionCategory
import kotlinx.coroutines.flow.Flow

interface TransactionCategoryManager {

    fun getCategories(transactionType: TransactionType): Flow<List<TransactionCategory>>

    suspend fun addCategory(category: TransactionCategory)

    suspend fun deleteCategory(id: Long)
}
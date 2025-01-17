package com.ancraz.mywallet.presentation.states

import com.ancraz.mywallet.presentation.models.TransactionCategoryUi

data class TransactionCategoriesState(
    val isLoading: Boolean = false,
    val incomeCategories: List<TransactionCategoryUi> = emptyList(),
    val expenseCategories: List<TransactionCategoryUi> = emptyList(),
    val error: String? = null
)
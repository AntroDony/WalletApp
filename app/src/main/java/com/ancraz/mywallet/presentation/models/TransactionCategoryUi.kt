package com.ancraz.mywallet.presentation.models

import com.ancraz.mywallet.core.models.TransactionType

data class TransactionCategoryUi(
    val id: Long = 0L,
    val name: String,
    val iconAssetPath: String,
    val transactionType: TransactionType
)
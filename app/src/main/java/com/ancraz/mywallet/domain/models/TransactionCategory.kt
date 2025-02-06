package com.ancraz.mywallet.domain.models

import com.ancraz.mywallet.core.models.TransactionType

data class TransactionCategory(
    val id: Long,
    val name: String,
    val iconName: String,
    val categoryType: TransactionType
)

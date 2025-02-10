package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.subModels.CategoryTransactionType
import com.ancraz.mywallet.domain.models.TransactionCategory

fun CategoryEntity.toTransactionCategory(): TransactionCategory{
    return TransactionCategory(
        id = this.id,
        name = this.name,
        iconName = this.iconName,
        categoryType = when(this.categoryType){
            CategoryTransactionType.INCOME -> TransactionType.INCOME
            CategoryTransactionType.EXPENSE -> TransactionType.EXPENSE
            CategoryTransactionType.TRANSFER -> TransactionType.TRANSFER
        }
    )
}


fun TransactionCategory.toCategoryEntity(): CategoryEntity{
    return CategoryEntity(
        id = this.id,
        name = this.name,
        iconName = this.iconName,
        categoryType = when (this.categoryType){
            TransactionType.INCOME -> CategoryTransactionType.INCOME
            TransactionType.EXPENSE -> CategoryTransactionType.EXPENSE
            TransactionType.TRANSFER -> CategoryTransactionType.TRANSFER
        }
    )
}
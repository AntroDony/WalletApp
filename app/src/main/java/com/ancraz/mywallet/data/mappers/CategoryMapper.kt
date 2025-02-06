package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.TransactionCategoryEntityType
import com.ancraz.mywallet.domain.models.TransactionCategory

fun CategoryEntity.toTransactionCategory(): TransactionCategory{
    return TransactionCategory(
        id = this.id,
        name = this.name,
        iconName = this.iconName,
        categoryType = when(this.categoryType){
            TransactionCategoryEntityType.INCOME -> TransactionType.INCOME
            TransactionCategoryEntityType.EXPENSE -> TransactionType.EXPENSE
            TransactionCategoryEntityType.TRANSFER -> TransactionType.TRANSFER
        }
    )
}


fun TransactionCategory.toCategoryEntity(): CategoryEntity{
    return CategoryEntity(
        id = this.id,
        name = this.name,
        iconName = this.iconName,
        categoryType = when (this.categoryType){
            TransactionType.INCOME -> TransactionCategoryEntityType.INCOME
            TransactionType.EXPENSE -> TransactionCategoryEntityType.EXPENSE
            TransactionType.TRANSFER -> TransactionCategoryEntityType.TRANSFER
        }
    )
}
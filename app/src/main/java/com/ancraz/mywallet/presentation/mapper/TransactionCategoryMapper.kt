package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi


fun TransactionCategory.toCategoryUi(): TransactionCategoryUi{
    return TransactionCategoryUi(
        id = this.id,
        name = this.name,
        iconAssetPath = "categories_icon/${this.iconName}.svg",
        transactionType = when(this.categoryType){
            TransactionType.INCOME -> TransactionType.INCOME
            TransactionType.EXPENSE -> TransactionType.EXPENSE
            TransactionType.TRANSFER -> TransactionType.TRANSFER
        }
    )
}

fun TransactionCategoryUi.toTransactionCategory(): TransactionCategory{
    return TransactionCategory(
        id = this.id,
        name = this.name,
        iconName = iconAssetPath.substringBefore(".svg").substringAfterLast("/"),
        categoryType = when(this.transactionType){
            TransactionType.INCOME -> TransactionType.INCOME
            TransactionType.EXPENSE -> TransactionType.EXPENSE
            TransactionType.TRANSFER -> TransactionType.TRANSFER
        }
    )
}
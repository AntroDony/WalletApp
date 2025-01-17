package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi


fun IncomeTransactionCategory.toCategoryUi(): TransactionCategoryUi{
    return TransactionCategoryUi(
        id = this.id,
        name = this.name,
        iconAssetPath = "categories_icon/${this.iconName}.svg",
        transactionType = TransactionType.INCOME
    )
}

fun ExpenseTransactionCategory.toCategoryUi(): TransactionCategoryUi{
    return TransactionCategoryUi(
        id = this.id,
        name = this.name,
        iconAssetPath = "categories_icon/${this.iconName}.svg",
        transactionType = TransactionType.EXPENSE
    )
}


fun TransactionCategoryUi.toIncomeCategory(): IncomeTransactionCategory{
    return IncomeTransactionCategory(
        id = this.id,
        name = this.name,
        iconName = iconAssetPath.substringBefore(".svg").substringAfterLast("/")
    )
}


fun TransactionCategoryUi.toExpenseCategory(): ExpenseTransactionCategory{
    return ExpenseTransactionCategory(
        id = this.id,
        name = this.name,
        iconName = iconAssetPath.substringBefore(".svg").substringAfterLast("/")
    )
}
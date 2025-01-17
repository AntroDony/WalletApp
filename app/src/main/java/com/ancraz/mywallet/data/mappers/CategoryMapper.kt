package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.data.storage.database.models.ExpenseCategoryEntity
import com.ancraz.mywallet.data.storage.database.models.IncomeCategoryEntity
import com.ancraz.mywallet.domain.models.ExpenseTransactionCategory
import com.ancraz.mywallet.domain.models.IncomeTransactionCategory

fun IncomeCategoryEntity.toTransactionCategory(): IncomeTransactionCategory{
    return IncomeTransactionCategory(
        id = this.id,
        name = this.name,
        iconName = this.iconName
    )
}


fun ExpenseCategoryEntity.toTransactionCategory(): ExpenseTransactionCategory{
    return ExpenseTransactionCategory(
        id = this.id,
        name = this.name,
        iconName = this.iconName
    )
}


fun IncomeTransactionCategory.toCategoryEntity(): IncomeCategoryEntity{
    return IncomeCategoryEntity(
        id = this.id,
        name = this.name,
        iconName = this.iconName
    )
}


fun ExpenseTransactionCategory.toCategoryEntity(): ExpenseCategoryEntity{
    return ExpenseCategoryEntity(
        id = this.id,
        name = this.name,
        iconName = this.iconName
    )
}
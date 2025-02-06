package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.data.storage.database.models.TransactionEntity
import com.ancraz.mywallet.domain.models.Transaction


fun TransactionEntity.toTransaction(): Transaction{
    return Transaction(
        id = this.id,
        time = this.time,
        value = this.value,
        currencyCode = this.currency,
        transactionType = this.transactionType,
        description = this.description,
        category = this.category?.toTransactionCategory()
    )
}


fun Transaction.toTransactionEntity(): TransactionEntity{
    return TransactionEntity(
        id = this.id,
        time = this.time,
        value = this.value,
        currency = this.currencyCode,
        transactionType = this.transactionType,
        description = this.description,
        category = this.category?.toCategoryEntity()
    )
}
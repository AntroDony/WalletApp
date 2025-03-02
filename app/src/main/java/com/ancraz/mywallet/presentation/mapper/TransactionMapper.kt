package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.presentation.models.TransactionUi

fun TransactionUi.toTransaction(): Transaction{
    return Transaction(
        id = this.id,
        time = this.time,
        value = this.value,
        currencyCode = this.currency,
        transactionType = this.type,
        description = this.description,
        category = this.category?.toTransactionCategory(),
        wallet = this.wallet?.toWallet(),
        selectedWalletAccount = this.selectedWalletAccount?.toCurrencyAccount()
    )
}


fun Transaction.toTransactionUi(): TransactionUi{
    return TransactionUi(
        id = this.id,
        time = this.time,
        value = this.value,
        currency = this.currencyCode,
        type = this.transactionType,
        description = this.description,
        category = this.category?.toCategoryUi(),
        wallet = this.wallet?.toWalletUi(),
        selectedWalletAccount = this.selectedWalletAccount?.toAccountUi()
    )
}
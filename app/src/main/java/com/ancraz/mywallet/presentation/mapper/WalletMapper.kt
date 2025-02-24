package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.presentation.models.WalletUi

fun Wallet.toWalletUi(): WalletUi {
    return WalletUi(
        id = this.id,
        name = this.name,
        description = this.description,
        accounts = this.currencyAccountList.map { account -> account.toAccountUi()},
        walletType = this.walletType,
        totalBalance = this.totalBalance
    )
}

fun WalletUi.toWallet(): Wallet{
    return Wallet(
        id = this.id,
        name = this.name,
        description = this.description,
        currencyAccountList = this.accounts.map { account -> account.toCurrencyAccount() },
        walletType = this.walletType,
        totalBalance = this.totalBalance
    )
}


private fun Wallet.WalletCurrencyAccount.toAccountUi(): WalletUi.CurrencyAccountUi {
    return WalletUi.CurrencyAccountUi(
        currency = this.currencyCode,
        moneyValue = this.value
    )
}


private fun WalletUi.CurrencyAccountUi.toCurrencyAccount(): Wallet.WalletCurrencyAccount {
    return Wallet.WalletCurrencyAccount(
        currencyCode = this.currency,
        value = this.moneyValue
    )
}
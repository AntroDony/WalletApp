package com.ancraz.mywallet.presentation.mapper

import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

fun Wallet.toWalletUi(): WalletUi {
    return WalletUi(
        id = this.id,
        name = this.name,
        description = this.description,
        accounts = this.currencyAccountList.map { account -> account.toAccountUi()},
        walletType = this.walletType,
        totalBalance = "${this.totalBalance.toFormattedString()} USD"
    )
}

fun WalletUi.toWallet(): Wallet{
    val totalBalance = this.accounts.map { it.moneyValue.toFloatValue() }.sum()

    return Wallet(
        id = this.id,
        name = this.name,
        description = this.description,
        currencyAccountList = this.accounts.map { account -> account.toCurrencyAccount() },
        walletType = this.walletType,
        totalBalance = totalBalance
    )
}


fun Wallet.WalletCurrencyAccount.toAccountUi(): WalletUi.CurrencyAccountUi {
    return WalletUi.CurrencyAccountUi(
        currency = this.currencyCode,
        moneyValue = this.value.toFormattedString()
    )
}


fun WalletUi.CurrencyAccountUi.toCurrencyAccount(): Wallet.WalletCurrencyAccount {
    return Wallet.WalletCurrencyAccount(
        currencyCode = this.currency,
        value = this.moneyValue.toFloatValue()
    )
}
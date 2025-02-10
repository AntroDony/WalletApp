package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.data.storage.database.models.WalletEntity
import com.ancraz.mywallet.data.storage.database.models.subModels.CurrencyAccount
import com.ancraz.mywallet.domain.models.Wallet


fun WalletEntity.toWallet(): Wallet{
    return Wallet(
        id = this.id,
        name = this.name,
        description = this.description,
        walletType = this.type,
        currencyAccountList = this.currencyAccountList.map { account -> account.toWalletCurrencyAccount() }
    )
}


fun Wallet.toWalletEntity(): WalletEntity{
    return WalletEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        type = this.walletType,
        currencyAccountList = this.currencyAccountList.map { account -> account.toCurrencyAccount() }
    )
}



private fun CurrencyAccount.toWalletCurrencyAccount(): Wallet.WalletCurrencyAccount{
    return Wallet.WalletCurrencyAccount(
        currencyCode = this.currencyCode,
        value = this.value
    )
}

private fun Wallet.WalletCurrencyAccount.toCurrencyAccount(): CurrencyAccount{
    return CurrencyAccount(
        currencyCode = this.currencyCode,
        value = this.value
    )
}
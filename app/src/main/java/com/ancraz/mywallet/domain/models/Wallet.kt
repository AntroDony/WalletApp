package com.ancraz.mywallet.domain.models

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType

data class Wallet(
    val id: Long,
    val name: String,
    val description: String?,
    val currencyAccountList: List<WalletCurrencyAccount>,
    val totalBalance: Float = 0f,
    val walletType: WalletType
){

    data class WalletCurrencyAccount(
        val currencyCode: CurrencyCode,
        val value: Float
    )
}
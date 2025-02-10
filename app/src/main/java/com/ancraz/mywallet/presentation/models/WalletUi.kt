package com.ancraz.mywallet.presentation.models

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType

data class WalletUi(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val accounts: List<CurrencyAccountUi>,
    val totalBalance: Float,
    val walletType: WalletType
) {
    data class CurrencyAccountUi(
        val currency: CurrencyCode = CurrencyCode.USD,
        val value: Float = 0f
    )
}
package com.ancraz.mywallet.presentation.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi


internal fun WalletType.getImageByWalletType(): ImageVector {
    return when(this){
        WalletType.CARD -> Icons.Filled.CreditCard
        WalletType.CASH -> Icons.Filled.Money
        WalletType.BANK_ACCOUNT -> Icons.Filled.AccountBalance
        WalletType.CRYPTO_WALLET -> Icons.Filled.CurrencyBitcoin
        WalletType.INVESTMENTS -> Icons.Filled.WaterfallChart
        WalletType.OTHER -> Icons.Filled.Payments
    }
}


internal fun WalletUi.getWalletCurrenciesString(): String{
    val currencyList = this.accounts.map { it.currency }

    return currencyList.joinToString(", ")
}
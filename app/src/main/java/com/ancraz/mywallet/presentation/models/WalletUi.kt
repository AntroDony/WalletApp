package com.ancraz.mywallet.presentation.models

import android.os.Parcelable
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletUi(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val accounts: List<CurrencyAccountUi>,
    val totalBalance: String,
    val walletType: WalletType
): Parcelable {

    @Parcelize
    data class CurrencyAccountUi(
        val currency: CurrencyCode = CurrencyCode.USD,
        val moneyValue: String = "0.00"
    ): Parcelable
}
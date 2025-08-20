package com.ancraz.mywallet.presentation.models

import android.os.Parcelable
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class TransactionUi(
    val id: Long = 0L,

    val time: Long = Calendar.getInstance().timeInMillis,

    val value: Float,

    val currency: CurrencyCode,

    val type: TransactionType,

    val description: String? = null,

    val category: TransactionCategoryUi? = null,

    val wallet: WalletUi? = null,

    val selectedWalletAccount: WalletUi.CurrencyAccountUi? = null
): Parcelable
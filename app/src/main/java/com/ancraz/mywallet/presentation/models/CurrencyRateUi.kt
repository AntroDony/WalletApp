package com.ancraz.mywallet.presentation.models

import android.os.Parcelable
import com.ancraz.mywallet.core.models.CurrencyCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyRateUi(
    val currencyCode: CurrencyCode,
    val rate: Float
): Parcelable
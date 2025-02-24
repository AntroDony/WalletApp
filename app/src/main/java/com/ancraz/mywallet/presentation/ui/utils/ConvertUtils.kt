package com.ancraz.mywallet.presentation.ui.utils

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.CurrencyRateUi
import com.ancraz.mywallet.presentation.models.WalletUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Float.toFormattedString(): String{
    return String.format(Locale.US, "%.2f", this)
}


fun Long.timeToString(): String {
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.US)
    val date = Date(this)
    return dateFormat.format(date)
}


fun String.toFloatValue(): Float {
    return try {
        this.toFloat()
    } catch (e: Exception) {
        debugLog("convert String to Float exception: ${e.message}")
        0f
    }
}

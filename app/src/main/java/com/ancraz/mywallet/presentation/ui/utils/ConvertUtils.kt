package com.ancraz.mywallet.presentation.ui.utils

import com.ancraz.mywallet.core.utils.debugLog
import java.util.Locale


fun Float.toFormattedString(): String{
    return String.format(Locale.US, "%.2f", this)
}

fun String.toFloatValue(): Float {
    return try {
        this.toFloat()
    } catch (e: Exception) {
        debugLog("convert String to Float exception: ${e.message}")
        0f
    }
}
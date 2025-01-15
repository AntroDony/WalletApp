package com.ancraz.mywallet.presentation.ui.utils

import java.util.Locale


fun Float.toFormattedString(): String{
    return String.format(Locale.US, "%.2f", this)
}
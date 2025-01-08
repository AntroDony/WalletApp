package com.ancraz.mywallet.core.utils

import android.util.Log
import com.ancraz.mywallet.BuildConfig

fun debugLog(log: String){
    if (BuildConfig.DEBUG){
        val stacktrace = Thread.currentThread().stackTrace[3]
        val methodName = stacktrace.methodName
        val className = stacktrace.className
        Log.e("WalletLog::", "$className.$methodName(): $log")

    }
}
package com.ancraz.mywallet.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun runOnUiThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}

fun runOnIOThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}


fun runOnDefaultThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch {
        block()
    }
}
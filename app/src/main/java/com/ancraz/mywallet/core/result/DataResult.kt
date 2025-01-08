package com.ancraz.mywallet.core.result

sealed class DataResult<T>(val data: T? = null, val errorMessage: String? = null){
    class Success<T>(data: T?): DataResult<T>(data = data)
    class Error<T>(message: String): DataResult<T>(errorMessage = message)
    class Loading<T>(): DataResult<T>()
}
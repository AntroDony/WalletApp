package com.ancraz.mywallet.core.utils

import com.ancraz.mywallet.BuildConfig
import com.ancraz.mywallet.core.utils.error.NetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError>{
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException){
        debugLog("EXCEPTION: ${e.message}")
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T>responseToResult(
    response: HttpResponse
): Result<T, NetworkError>{
    return when(response.status.value){
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException){
                debugLog("Exception: ${e.message}")
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }

}

fun constructUrl(url: String): String {
    return when{
        url.contains(BuildConfig.API_BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.API_BASE_URL + url.drop(1)
        else -> BuildConfig.API_BASE_URL + url
    }
}
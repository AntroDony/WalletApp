package com.ancraz.mywallet.data.network

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.constructUrl
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.core.utils.map
import com.ancraz.mywallet.core.utils.safeCall
import com.ancraz.mywallet.data.mappers.toCurrencyData
import com.ancraz.mywallet.domain.models.CurrencyData
import com.ancraz.mywallet.domain.network.CurrencyDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CurrencyRateDataSource(
    private val httpClient: HttpClient,
    private val apiKey: String
): CurrencyDataSource {

    override suspend fun getDesiredCurrenciesRate(
        desiredCurrenciesString: String,
        baseCurrencyCode: String
    ): Result<CurrencyData, NetworkError> {
        try {
            val response = httpClient.get(
                constructUrl("/rates/latest")
            ){
                parameter("apikey", apiKey)
                parameter("base", baseCurrencyCode)
                parameter("symbols", desiredCurrenciesString)
            }

            return safeCall<String> {
                response
            }.map { responseStr ->
                responseStr.toCurrencyData()
            }
        } catch (e: Exception){
            debugLog("getDesiredCurrenciesRate exception: ${e.message}")
            return Result.Error(error = NetworkError.UNKNOWN)
        }

    }
}
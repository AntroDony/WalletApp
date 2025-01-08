package com.ancraz.mywallet.data.network

import com.ancraz.mywallet.core.utils.Result
import com.ancraz.mywallet.core.utils.constructUrl
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.core.utils.error.NetworkError
import com.ancraz.mywallet.core.utils.map
import com.ancraz.mywallet.core.utils.safeCall
import com.ancraz.mywallet.data.mappers.toCurrencyRates
import com.ancraz.mywallet.data.models.ApiResponseDto
import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.domain.network.NetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class CurrencyRateDataSource(
    private val httpClient: HttpClient,
    private val apiKey: String
): NetworkDataSource {

    override suspend fun getDesiredCurrenciesRate(
        desiredCurrenciesString: String,
        baseCurrencyCode: String
    ): Result<List<CurrencyRate>, NetworkError> {
        val response = httpClient.get(
            constructUrl("/rates/latest")
        ){
            parameter("apikey", apiKey)
            parameter("base", baseCurrencyCode)
            parameter("symbols", desiredCurrenciesString)
        }

        debugLog("RESPONSE: ${response.body<String>()} | $response")

        return safeCall<String> {
            response
        }.map { responseStr ->
            responseStr.toCurrencyRates()
        }
    }
}
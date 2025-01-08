package com.ancraz.mywallet.data.mappers

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.CurrencyCode
import com.ancraz.mywallet.domain.models.CurrencyRate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun String.toCurrencyRates(): List<CurrencyRate>{
    try {
        val jsonResult = Json.parseToJsonElement(this).jsonObject
        val date = jsonResult["date"]?.jsonPrimitive?.content?.toTimestamp() ?: Calendar.getInstance().timeInMillis
        val rates = jsonResult["rates"]?.jsonObject ?: throw IllegalArgumentException("No rates found")

        val currencyRateList = rates.map { (key, value) ->
            CurrencyRate(
                updateDate = date,
                currencyCode = key.toCurrencyCode(),
                rateValue = value.jsonPrimitive.content.toFloat()
            )
        }
        return currencyRateList
    }
    catch (e: Exception){
        debugLog("getCurrencyRates exception: ${e.message}")
        return emptyList()
    }
}


fun String.toCurrencyCode(): CurrencyCode {
    return when(this){
        "USD" -> CurrencyCode.USD
        "EUR" -> CurrencyCode.EUR
        "RUB" -> CurrencyCode.RUB
        "GEL" -> CurrencyCode.GEL
        "KZT" -> CurrencyCode.KZT
        else -> CurrencyCode.UNKNOWN
    }
}


private fun String.toTimestamp(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ssX", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC") // Ensure UTC timezone

    val timestamp = format.parse(this)

    return timestamp?.time ?: Calendar.getInstance().timeInMillis
}
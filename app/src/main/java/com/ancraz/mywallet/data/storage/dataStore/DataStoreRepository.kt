package com.ancraz.mywallet.data.storage.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.CurrencyCode
import com.ancraz.mywallet.domain.models.CurrencyRate
import kotlinx.coroutines.flow.first

class DataStoreRepository(
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet_data_store")

    private val CURRENCY_UPDATE_TIME = longPreferencesKey("currency_update_time")
    private val EURO_RATE_TO_USD = floatPreferencesKey("euro_rate_to_usd")
    private val RUB_RATE_TO_USD = floatPreferencesKey("rub_rate_to_usd")
    private val GEL_RATE_TO_USD = floatPreferencesKey("gel_rate_to_usd")
    private val KZT_RATE_TO_USD = floatPreferencesKey("kzt_rate_to_usd")


    suspend fun getCurrencyLastUpdateTime(): Long? {
        return context.dataStore.data.first()[CURRENCY_UPDATE_TIME]
    }

    suspend fun getCurrencyRateToUsd(currencyCode: CurrencyCode): Float?{
        when (currencyCode) {
            CurrencyCode.EUR -> {
                return context.dataStore.data.first()[EURO_RATE_TO_USD]
            }

            CurrencyCode.RUB -> {
                return context.dataStore.data.first()[RUB_RATE_TO_USD]
            }

            CurrencyCode.GEL -> {
                return context.dataStore.data.first()[GEL_RATE_TO_USD]
            }

            CurrencyCode.KZT -> {
                return context.dataStore.data.first()[KZT_RATE_TO_USD]
            }

            CurrencyCode.USD, CurrencyCode.UNKNOWN -> {
                return null
            }
        }
    }


    suspend fun updateLastCurrencyUpdatedTime(timeInMillis: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_UPDATE_TIME] = timeInMillis
        }
    }

    suspend fun updateCurrencyRate(rate: CurrencyRate) {
        context.dataStore.edit { preferences ->
            when (rate.currencyCode) {
                CurrencyCode.EUR -> {
                    preferences[EURO_RATE_TO_USD] = rate.rateValue
                }

                CurrencyCode.RUB -> {
                    preferences[RUB_RATE_TO_USD] = rate.rateValue
                }

                CurrencyCode.GEL -> {
                    preferences[GEL_RATE_TO_USD] = rate.rateValue
                }

                CurrencyCode.KZT -> {
                    preferences[KZT_RATE_TO_USD] = rate.rateValue
                }

                CurrencyCode.USD, CurrencyCode.UNKNOWN -> {
                    debugLog("cannot update UNKNOWN or USD currency code")
                }
            }
        }
    }
}
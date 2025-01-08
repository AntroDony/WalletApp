package com.ancraz.mywallet.data.storage

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet_data_store")

    private val CURRENCY_UPDATE_TIME = longPreferencesKey("currency_update_time")
    private val EURO_RATE_TO_USD = floatPreferencesKey("euro_rate_to_usd")
    private val RUB_RATE_TO_USD = floatPreferencesKey("rub_rate_to_usd")
    private val GEL_RATE_TO_USD = floatPreferencesKey("gel_rate_to_usd")
    private val KZT_RATE_TO_USD = floatPreferencesKey("kzt_rate_to_usd")


    val updateCurrencyTime: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENCY_UPDATE_TIME]
        }

    val euroRate: Flow<Float?> = context.dataStore.data
        .map { preferences ->
            preferences[EURO_RATE_TO_USD]
        }

    val rubRate: Flow<Float?> = context.dataStore.data
        .map { preferences ->
            preferences[RUB_RATE_TO_USD]
        }

    val gelRate: Flow<Float?> = context.dataStore.data
        .map { preferences ->
            preferences[GEL_RATE_TO_USD]
        }

    val kztRate: Flow<Float?> = context.dataStore.data
        .map { preferences ->
            preferences[KZT_RATE_TO_USD]
        }


    suspend fun updateLastCurrencyUpdatedTime(timeInMillis: Long){
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_UPDATE_TIME] = timeInMillis
        }
    }

    suspend fun updateCurrencyRates(rates: List<CurrencyRate>){
        context.dataStore.edit { preferences ->
            rates.forEach { currencyRate ->
                when(currencyRate.currencyCode){
                    CurrencyCode.EUR -> {
                        preferences[EURO_RATE_TO_USD] = currencyRate.rateValue
                    }
                    CurrencyCode.RUB -> {
                        preferences[RUB_RATE_TO_USD] = currencyRate.rateValue
                    }
                    CurrencyCode.GEL -> {
                        preferences[GEL_RATE_TO_USD] = currencyRate.rateValue
                    }
                    CurrencyCode.KZT -> {
                        preferences[KZT_RATE_TO_USD] = currencyRate.rateValue
                    }
                    CurrencyCode.USD, CurrencyCode.UNKNOWN -> {
                        debugLog("cannot update UNKNOWN or USD currency code")
                    }
                }
            }
        }
    }
}
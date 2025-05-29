package com.ancraz.mywallet.data.storage.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.utils.Constants
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet_data_store")

    private val TOTAL_BALANCE_USD = floatPreferencesKey(Constants.Prefs.TOTAL_BALANCE_KEY)
    private val PRIVATE_MODE_STATUS = booleanPreferencesKey(Constants.Prefs.PRIVATE_MODE_KEY)
    private val RECENT_WALLET_ACCOUNT_ID = longPreferencesKey(Constants.Prefs.RECENT_WALLET_KEY)
    private val RECENT_CURRENCY_NAME = stringPreferencesKey(Constants.Prefs.RECENT_CURRENCY_KEY)

    private val CURRENCY_UPDATE_TIME = longPreferencesKey(Constants.Prefs.CURRENCY_UPDATE_TIME_KEY)
    private val EURO_RATE_TO_USD = floatPreferencesKey(Constants.Prefs.EURO_RATE_KEY)
    private val RUB_RATE_TO_USD = floatPreferencesKey(Constants.Prefs.RUB_RATE_KEY)
    private val GEL_RATE_TO_USD = floatPreferencesKey(Constants.Prefs.GEL_RATE_KEY)
    private val KZT_RATE_TO_USD = floatPreferencesKey(Constants.Prefs.KZT_RATE_KEY)


    fun totalBalanceInUsdFlow(): Flow<Float> {
        return context.dataStore.data
            .map { preferences ->
                preferences[TOTAL_BALANCE_USD] ?: 0f
            }
    }

    suspend fun updateTotalBalanceInUsd(newValue: Float){
        context.dataStore.edit { preferences ->
            preferences[TOTAL_BALANCE_USD] = newValue
        }
    }

    suspend fun incomeTotalBalance(value: Float){
        context.dataStore.edit { preferences ->
            preferences[TOTAL_BALANCE_USD] = preferences[TOTAL_BALANCE_USD]?.let {
                it + value
            } ?: (0f + value)
        }
    }

    suspend fun expenseTotalBalance(value: Float){
        context.dataStore.edit { preferences ->
            preferences[TOTAL_BALANCE_USD] = preferences[TOTAL_BALANCE_USD]?.let {
                it - value
            } ?: (0f - value)
        }
    }

    suspend fun getCurrencyLastUpdateTime(): Long? {
        return context.dataStore.data.first()[CURRENCY_UPDATE_TIME]
    }

    suspend fun getCurrencyRateToUsd(currencyCode: CurrencyCode): Float{
        when (currencyCode) {
            CurrencyCode.EUR -> {
                return context.dataStore.data.first()[EURO_RATE_TO_USD] ?: 0f
            }

            CurrencyCode.RUB -> {
                return context.dataStore.data.first()[RUB_RATE_TO_USD] ?: 0f
            }

            CurrencyCode.GEL -> {
                return context.dataStore.data.first()[GEL_RATE_TO_USD] ?: 0f
            }

            CurrencyCode.KZT -> {
                return context.dataStore.data.first()[KZT_RATE_TO_USD] ?: 0f
            }

            CurrencyCode.USD -> {
                return 1f
            }
        }
    }


    suspend fun setLastCurrencyUpdatedTime(timeInMillis: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_UPDATE_TIME] = timeInMillis
        }
    }

    suspend fun setCurrencyRate(rate: CurrencyRate) {
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

                CurrencyCode.USD -> {
                    debugLog("cannot update UNKNOWN or USD currency code")
                }
            }
        }
    }


    fun getRecentWalletIdFlow(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[RECENT_WALLET_ACCOUNT_ID] ?: 0L
        }
    }

    suspend fun setRecentWalletId(id: Long){
        context.dataStore.edit { preferences ->
            preferences[RECENT_WALLET_ACCOUNT_ID] = id
        }
    }

    fun getRecentCurrencyNameFlow(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[RECENT_CURRENCY_NAME] ?: "USD"
        }
    }

    suspend fun setRecentCurrencyName(name: String){
        context.dataStore.edit { preferences ->
            preferences[RECENT_CURRENCY_NAME] = name
        }
    }


    fun getPrivateModeStatus(): Flow<Boolean>{
        return context.dataStore.data.map { preferences ->
            preferences[PRIVATE_MODE_STATUS] ?: false
        }
    }


    suspend fun updatePrivateModeStatus(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PRIVATE_MODE_STATUS] = value
        }
    }
}
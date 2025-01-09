package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.core.models.CurrencyCode

class CurrencyConverter {

    @TypeConverter
    fun currencyCodeToString(currencyCodeEntity: CurrencyCode): String{
        return when(currencyCodeEntity){
            CurrencyCode.USD -> CurrencyCode.USD.name
            CurrencyCode.EUR -> CurrencyCode.EUR.name
            CurrencyCode.RUB -> CurrencyCode.RUB.name
            CurrencyCode.GEL -> CurrencyCode.GEL.name
            CurrencyCode.KZT -> CurrencyCode.KZT.name
        }
    }


    @TypeConverter
    fun stringToCurrencyCode(currencyStr: String): CurrencyCode {
        return when(currencyStr){
            CurrencyCode.EUR.name -> CurrencyCode.EUR
            CurrencyCode.RUB.name -> CurrencyCode.RUB
            CurrencyCode.GEL.name -> CurrencyCode.GEL
            CurrencyCode.KZT.name -> CurrencyCode.KZT
            else -> CurrencyCode.USD
        }
    }
}
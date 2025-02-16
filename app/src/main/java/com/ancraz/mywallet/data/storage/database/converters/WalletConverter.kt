package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.data.storage.database.models.subModels.CurrencyAccount
import com.ancraz.mywallet.core.models.WalletType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WalletConverter {

    private val gson = Gson()

    @TypeConverter
    fun currencyAccountListToString(account: List<CurrencyAccount>): String {
        return gson.toJson(account)
    }


    @TypeConverter
    fun stringToCurrencyAccountList(accountString: String): List<CurrencyAccount>{
        val groupType = object : TypeToken<CurrencyAccount>(){}.type
        return gson.fromJson(accountString, groupType)
    }


    @TypeConverter
    fun walletTypeToString(walletType: WalletType): String {
        return walletType.name
    }


    @TypeConverter
    fun stringToWalletType(walletTypeString: String): WalletType {
        return when(walletTypeString){
            WalletType.CARD.name -> WalletType.CARD
            WalletType.CASH.name -> WalletType.CASH
            WalletType.BANK_ACCOUNT.name -> WalletType.BANK_ACCOUNT
            WalletType.CRYPTO_WALLET.name -> WalletType.CRYPTO_WALLET
            WalletType.INVESTMENTS.name -> WalletType.INVESTMENTS
            else -> WalletType.CASH
        }
    }
}
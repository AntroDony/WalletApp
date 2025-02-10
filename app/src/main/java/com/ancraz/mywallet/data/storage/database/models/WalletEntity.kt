package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ancraz.mywallet.data.storage.database.models.subModels.CurrencyAccount
import com.ancraz.mywallet.core.models.WalletType

@Keep
@Entity(tableName = "wallets")
data class WalletEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    val id: Long = 0L,

    @ColumnInfo(name = "wallet_name")
    val name: String,

    @ColumnInfo(name = "wallet_description")
    val description: String? = null,

    @ColumnInfo(name = "currency_accounts")
    val currencyAccountList: List<CurrencyAccount>,

    @ColumnInfo(name = "wallet_type")
    val type: WalletType
)
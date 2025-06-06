package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType

@Keep
@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val id: Long = 0L,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "value")
    val value: Float,

    @ColumnInfo(name = "currency")
    val currency: CurrencyCode,

    @ColumnInfo(name = "transaction_type")
    val transactionType: TransactionType,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "category")
    val category: CategoryEntity? = null,

    @ColumnInfo(name = "wallet_account_id")
    val walletId: Long? = null

//    @ColumnInfo(name = "to_account")
//    val toAccount: BalanceAccountEntity? = null,
//
//    @ColumnInfo(name = "from_account")
//    val fromAccount: BalanceAccountEntity? = null

)
package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType

@Keep
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = BalanceAccountEntity::class,
            parentColumns = arrayOf("account_id"),
            childColumns = arrayOf("account_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ])
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val id: Long = 0L,

    @ColumnInfo(name = "account_id")
    val accountId: Long,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "value")
    val value: Float,

    @ColumnInfo(name = "currency")
    val currency: CurrencyCode,

    @ColumnInfo(name = "transaction_type")
    val transactionType: TransactionType,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "category")
    val category: BaseCategory
)
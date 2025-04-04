package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.core.models.TransactionType

class TransactionTypeConverter {

    @TypeConverter
    fun transactionTypeToString(type: TransactionType): String{
        return when(type){
            TransactionType.INCOME -> TransactionType.INCOME.name
            TransactionType.EXPENSE -> TransactionType.EXPENSE.name
            TransactionType.TRANSFER -> TransactionType.TRANSFER.name
        }
    }


    @TypeConverter
    fun stringToTransactionType(typeStr: String): TransactionType {
        return when(typeStr){
            TransactionType.EXPENSE.name -> TransactionType.EXPENSE
            TransactionType.TRANSFER.name -> TransactionType.TRANSFER
            else -> TransactionType.INCOME
        }
    }
}
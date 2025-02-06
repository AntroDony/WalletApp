package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.data.storage.database.models.TransactionCategoryEntityType

class CategoryTypeConverter {

    @TypeConverter
    fun categoryTypeToString(type: TransactionCategoryEntityType): String{
        return type.name
    }


    @TypeConverter
    fun stringToCategoryType(typeName: String): TransactionCategoryEntityType {
        return when(typeName){
            TransactionCategoryEntityType.INCOME.name -> TransactionCategoryEntityType.INCOME
            else -> TransactionCategoryEntityType.EXPENSE
        }
    }

}
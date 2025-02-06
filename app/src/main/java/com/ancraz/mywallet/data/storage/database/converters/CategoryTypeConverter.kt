package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.data.storage.database.models.CategoryTransactionType

class CategoryTypeConverter {

    @TypeConverter
    fun categoryTypeToString(type: CategoryTransactionType): String{
        return type.name
    }


    @TypeConverter
    fun stringToCategoryType(typeName: String): CategoryTransactionType {
        return when(typeName){
            CategoryTransactionType.INCOME.name -> CategoryTransactionType.INCOME
            else -> CategoryTransactionType.EXPENSE
        }
    }

}
package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.subModels.CategoryTransactionType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryConverter {

    private val gson = Gson()

    @TypeConverter
    fun categoryEntityToString(categoryEntity: CategoryEntity?): String{
        return gson.toJson(categoryEntity)
    }

    @TypeConverter
    fun stringToCategoryEntity(categoryString: String): CategoryEntity?{
        val groupType = object : TypeToken<CategoryEntity>(){}.type
        return gson.fromJson(categoryString, groupType)
    }

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
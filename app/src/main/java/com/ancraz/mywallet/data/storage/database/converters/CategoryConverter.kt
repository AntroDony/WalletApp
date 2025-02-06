package com.ancraz.mywallet.data.storage.database.converters

import androidx.room.TypeConverter
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryConverter {

    private val gson = Gson()

    @TypeConverter
    fun categoryEntityToString(categoryEntity: CategoryEntity): String{
        return gson.toJson(categoryEntity)
    }

    @TypeConverter
    fun stringToCategoryEntity(categoryString: String): CategoryEntity{
        val groupType = object : TypeToken<CategoryEntity>(){}.type
        return gson.fromJson(categoryString, groupType)
    }
}
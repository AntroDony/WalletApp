package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "categories")
data class CategoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val id: Long = 0L,

    @ColumnInfo(name = "category_name")
    val name: String,

    @ColumnInfo(name = "category_icon")
    val iconName: String,

    @ColumnInfo(name = "category_type")
    val categoryType: CategoryTransactionType
)

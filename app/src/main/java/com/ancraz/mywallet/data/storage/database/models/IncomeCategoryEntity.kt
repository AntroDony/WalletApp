package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "income_categories")
data class IncomeCategoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    override val id: Long,

    @ColumnInfo(name = "category_name")
    override val name: String,

): BaseCategoryEntity(id, name)

package com.ancraz.mywallet.data.storage.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "expense_categories")
data class ExpenseCategoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    override val id: Long,

    @ColumnInfo(name = "category_name")
    override val name: String,

): BaseCategory(id, name)
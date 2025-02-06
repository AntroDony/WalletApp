package com.ancraz.mywallet.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.CategoryTransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE category_type = :categoryType")
    fun getAllIncomeCategories(categoryType: CategoryTransactionType = CategoryTransactionType.INCOME): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE category_type = :categoryType")
    fun getAllExpenseCategories(categoryType: CategoryTransactionType = CategoryTransactionType.EXPENSE): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: CategoryEntity): Long

    @Query("DELETE FROM categories WHERE category_id = :id")
    fun deleteCategoryById(id: Long)
}
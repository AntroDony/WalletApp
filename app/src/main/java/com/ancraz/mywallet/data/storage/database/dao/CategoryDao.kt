package com.ancraz.mywallet.data.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ancraz.mywallet.data.storage.database.models.ExpenseCategoryEntity
import com.ancraz.mywallet.data.storage.database.models.IncomeCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM income_categories")
    fun getAllIncomeCategories(): Flow<List<IncomeCategoryEntity>>

    @Query("SELECT * FROM expense_categories")
    fun getAllExpenseCategories(): Flow<List<ExpenseCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncomeCategory(category: IncomeCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenseCategory(category: ExpenseCategoryEntity): Long

    @Query("DELETE FROM income_categories WHERE category_id = :id")
    fun deleteIncomeCategoryById(id: Long)

    @Query("DELETE FROM expense_categories WHERE category_id = :id")
    fun deleteExpenseCategoryById(id: Long)
}
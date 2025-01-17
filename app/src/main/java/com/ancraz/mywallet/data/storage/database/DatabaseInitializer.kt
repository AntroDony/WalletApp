package com.ancraz.mywallet.data.storage.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.models.ExpenseCategoryEntity
import com.ancraz.mywallet.data.storage.database.models.IncomeCategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class DatabaseInitializer(
    private val dataProvider: Provider<CategoryDao>
): RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }


    private fun populateDatabase(){
        expenseCategories.forEach { category ->
            dataProvider.get().insertExpenseCategory(category)
        }

        incomeCategories.forEach { category ->
            dataProvider.get().insertIncomeCategory(category)
        }

    }


    private val expenseCategories = listOf(
        ExpenseCategoryEntity(id = 1, name = "Products", iconName = "products_category"),
        ExpenseCategoryEntity(id = 2, name = "Restaurants", iconName = "restaurants_category"),
        ExpenseCategoryEntity(id = 3, name = "Clothes", iconName = "clothes_category"),
        ExpenseCategoryEntity(id = 4, name = "Health", iconName = "health_category"),
        ExpenseCategoryEntity(id = 5, name = "Transport", iconName = "transport_category"),
        ExpenseCategoryEntity(id = 6, name = "House", iconName = "house_category"),
        ExpenseCategoryEntity(id = 7, name = "Entertainment", iconName = "entertainment_category"),
        ExpenseCategoryEntity(id = 8, name = "Education", iconName = "education_category"),
        ExpenseCategoryEntity(id = 9, name = "Sport", iconName = "sport_category"),
        ExpenseCategoryEntity(id = 10, name = "Bills", iconName = "bills_category"),
        ExpenseCategoryEntity(id = 11, name = "Toiletry", iconName = "toiletry_category"),
        ExpenseCategoryEntity(id = 12, name = "Communication", iconName = "communication_category"),
        ExpenseCategoryEntity(id = 13, name = "Gifts", iconName = "gifts_category"),
        ExpenseCategoryEntity(id = 14, name = "Investments", iconName = "investments_category"),
        ExpenseCategoryEntity(id = 15, name = "Other", iconName = "other_category")
    )


    private val incomeCategories = listOf(
        IncomeCategoryEntity(id = 1, name = "Salary", iconName = "salary_category"),
        IncomeCategoryEntity(id = 2, name = "Deposit", iconName = "deposit_category"),
        IncomeCategoryEntity(id = 3, name = "Savings", iconName = "savings_category"),
        IncomeCategoryEntity(id = 4, name = "Other", iconName = "other_category")
    )
}

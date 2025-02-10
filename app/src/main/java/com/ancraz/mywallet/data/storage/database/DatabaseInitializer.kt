package com.ancraz.mywallet.data.storage.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.subModels.CategoryTransactionType
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
            dataProvider.get().insertCategory(category)
        }

        incomeCategories.forEach { category ->
            dataProvider.get().insertCategory(category)
        }

    }


    private val expenseCategories = listOf(
        CategoryEntity(id = 1, name = "Products", iconName = "products_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 2, name = "Restaurants", iconName = "restaurants_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 3, name = "Clothes", iconName = "clothes_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 4, name = "Health", iconName = "health_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 5, name = "Transport", iconName = "transport_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 6, name = "House", iconName = "house_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 7, name = "Entertainment", iconName = "entertainment_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 8, name = "Education", iconName = "education_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 9, name = "Sport", iconName = "sport_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 10, name = "Bills", iconName = "bills_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 11, name = "Toiletry", iconName = "toiletry_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 12, name = "Communication", iconName = "communication_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 13, name = "Gifts", iconName = "gifts_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 14, name = "Investments", iconName = "investments_category", CategoryTransactionType.EXPENSE),
        CategoryEntity(id = 15, name = "Other", iconName = "other_category", CategoryTransactionType.EXPENSE)
    )


    private val incomeCategories = listOf(
        CategoryEntity(id = 16, name = "Salary", iconName = "salary_category", CategoryTransactionType.INCOME),
        CategoryEntity(id = 17, name = "Deposit", iconName = "deposit_category", CategoryTransactionType.INCOME),
        CategoryEntity(id = 18, name = "Savings", iconName = "savings_category", CategoryTransactionType.INCOME),
        CategoryEntity(id = 19, name = "Other", iconName = "other_category", CategoryTransactionType.INCOME)
    )
}

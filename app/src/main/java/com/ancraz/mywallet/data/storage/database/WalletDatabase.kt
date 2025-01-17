package com.ancraz.mywallet.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ancraz.mywallet.data.storage.database.converters.CurrencyConverter
import com.ancraz.mywallet.data.storage.database.converters.TransactionTypeConverter
import com.ancraz.mywallet.data.storage.database.dao.AccountDao
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.data.storage.database.models.BalanceAccountEntity
import com.ancraz.mywallet.data.storage.database.models.ExpenseCategoryEntity
import com.ancraz.mywallet.data.storage.database.models.IncomeCategoryEntity
import com.ancraz.mywallet.data.storage.database.models.TransactionEntity

@Database(
    entities = [
        BalanceAccountEntity::class,
        TransactionEntity::class,
        IncomeCategoryEntity::class,
        ExpenseCategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(CurrencyConverter::class, TransactionTypeConverter::class)
abstract class WalletDatabase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao
}
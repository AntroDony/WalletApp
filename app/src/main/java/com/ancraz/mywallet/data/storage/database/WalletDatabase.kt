package com.ancraz.mywallet.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ancraz.mywallet.data.storage.database.converters.CategoryConverter
import com.ancraz.mywallet.data.storage.database.converters.CurrencyConverter
import com.ancraz.mywallet.data.storage.database.converters.TransactionTypeConverter
import com.ancraz.mywallet.data.storage.database.converters.WalletConverter
import com.ancraz.mywallet.data.storage.database.dao.AccountDao
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.data.storage.database.dao.WalletDao
import com.ancraz.mywallet.data.storage.database.models.BalanceAccountEntity
import com.ancraz.mywallet.data.storage.database.models.CategoryEntity
import com.ancraz.mywallet.data.storage.database.models.TransactionEntity
import com.ancraz.mywallet.data.storage.database.models.WalletEntity

@Database(
    entities = [
        BalanceAccountEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        WalletEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    CurrencyConverter::class,
    TransactionTypeConverter::class,
    WalletConverter::class,
    CategoryConverter::class
)
abstract class WalletDatabase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    abstract fun walletDao(): WalletDao
}
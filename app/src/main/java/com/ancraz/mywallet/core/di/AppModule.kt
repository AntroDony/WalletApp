package com.ancraz.mywallet.core.di

import android.content.Context
import androidx.room.Room
import com.ancraz.mywallet.BuildConfig
import com.ancraz.mywallet.data.network.CurrencyRateDataSource
import com.ancraz.mywallet.data.repository.TransactionRepositoryImpl
import com.ancraz.mywallet.data.repository.CurrencyRepositoryImpl
import com.ancraz.mywallet.data.repository.WalletRepositoryImpl
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.data.storage.database.DatabaseInitializer
import com.ancraz.mywallet.data.storage.database.WalletDatabase
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.data.storage.database.dao.WalletDao
import com.ancraz.mywallet.domain.network.CurrencyDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWalletDatabase(
        @ApplicationContext context: Context,
        categoryProvider: Provider<CategoryDao>
    ): WalletDatabase {
        val walletDatabase = Room.databaseBuilder(
            context,
            WalletDatabase::class.java,
            "wallet_database.db"
        ).addCallback(
            DatabaseInitializer(dataProvider = categoryProvider)
        ).fallbackToDestructiveMigration()
            .build()

        return walletDatabase
    }


    @Provides
    @Singleton
    fun provideCurrencyDataSource(): CurrencyDataSource {
        return CurrencyRateDataSource(
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
            },
            BuildConfig.API_KEY
        )
    }


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepository(context)
    }

    @Provides
    @Singleton
    fun provideTransactionDao(walletDatabase: WalletDatabase): TransactionDao {
        return walletDatabase.transactionDao()
    }


    @Provides
    @Singleton
    fun provideCategoryDao(walletDatabase: WalletDatabase): CategoryDao{
        return walletDatabase.categoryDao()
    }


    @Provides
    @Singleton
    fun provideWalletDao(walletDatabase: WalletDatabase): WalletDao{
        return walletDatabase.walletDao()
    }

}
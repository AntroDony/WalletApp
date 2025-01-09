package com.ancraz.mywallet.core.di

import android.content.Context
import androidx.room.Room
import com.ancraz.mywallet.BuildConfig
import com.ancraz.mywallet.data.network.CurrencyRateDataSource
import com.ancraz.mywallet.data.repository.CurrencyRepositoryImpl
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.network.NetworkDataSource
import com.ancraz.mywallet.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        val taskDatabase = Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            Constants.TASK_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        return taskDatabase
    }


    @Provides
    @Singleton
    fun provideCurrencyRateDataSource(): NetworkDataSource{
        return CurrencyRateDataSource(
            HttpClient(CIO){
                install(ContentNegotiation){
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
    fun provideCurrencyRepository(dataSource: NetworkDataSource): CurrencyRepository{
        return CurrencyRepositoryImpl(dataSource)
    }

}
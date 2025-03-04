package com.ancraz.mywallet.core.di

import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.manager.TransactionCategoryManager
import com.ancraz.mywallet.domain.manager.TransactionManager
import com.ancraz.mywallet.domain.manager.WalletManager
import com.ancraz.mywallet.presentation.manager.DataStoreManagerImpl
import com.ancraz.mywallet.presentation.manager.TransactionCategoryManagerImpl
import com.ancraz.mywallet.presentation.manager.TransactionManagerImpl
import com.ancraz.mywallet.presentation.manager.WalletManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    abstract fun bindTransactionManager(impl: TransactionManagerImpl): TransactionManager

    @Binds
    abstract fun bindTransactionCategoryManager(impl: TransactionCategoryManagerImpl): TransactionCategoryManager

    @Binds
    abstract fun bindWalletManager(impl: WalletManagerImpl): WalletManager

    @Binds
    abstract fun bindDataStoreManager(impl: DataStoreManagerImpl): DataStoreManager
}
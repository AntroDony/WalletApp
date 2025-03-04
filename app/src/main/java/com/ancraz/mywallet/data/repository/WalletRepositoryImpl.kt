package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.mappers.toWallet
import com.ancraz.mywallet.data.mappers.toWalletEntity
import com.ancraz.mywallet.data.storage.database.dao.WalletDao
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val dao: WalletDao
) : WalletRepository {

    override fun getWalletList(): Flow<List<Wallet>> {
        return dao.getAllWallets().map { list ->
            list.map { wallet ->
                wallet.toWallet()
            }
        }
    }

    override suspend fun getWalletById(id: Long): Wallet {
        return dao.getWalletById(id).toWallet()
    }

    override suspend fun addWallet(wallet: Wallet) {
        dao.addNewWallet(wallet.toWalletEntity())
    }

    override suspend fun updateWallet(wallet: Wallet) {
        dao.updateWallet(wallet.toWalletEntity())
    }

    override suspend fun deleteWalletById(id: Long) {
        dao.deleteWalletById(id)
    }


    override suspend fun incomeToWallet(
        wallet: Wallet,
        selectedAccount: Wallet.WalletCurrencyAccount,
        value: Float
    ) {
        val newAccountList = wallet.currencyAccountList.map { account ->
            if (account == selectedAccount){
                account.copy(value = account.value + value)
            } else {
                account
            }
        }

        updateWallet(
            wallet.copy(
                currencyAccountList = newAccountList
            )
        )
    }


    override suspend fun expenseFromWallet(
        wallet: Wallet,
        selectedAccount: Wallet.WalletCurrencyAccount,
        value: Float
    ) {
        val newAccountList = wallet.currencyAccountList.map { account ->
            if (account == selectedAccount){
                account.copy(value = account.value - value)
            } else {
                account
            }
        }

        updateWallet(
            wallet.copy(
                currencyAccountList = newAccountList
            )
        )
    }
}
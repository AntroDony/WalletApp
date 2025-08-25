package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.domain.converter.CurrencyConverter
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllWalletsUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    private val currencyConverter = CurrencyConverter(dataStoreRepository)

    operator fun invoke(): Flow<List<Wallet>> {
        return try {
            repository.getWalletList().map { walletList ->
                walletList.map { wallet ->
                    wallet.copy(
                        totalBalance = getTotalBalance(wallet.currencyAccountList)
                    )
                }
            }
        } catch (e: Exception) {
            debugLog("getAllWalletsUseCase exception: ${e.message}")
            flowOf(emptyList())
        }
    }


    private suspend fun getTotalBalance(currencyAccountList: List<Wallet.WalletCurrencyAccount>): Float {
        var totalBalance = 0f

        currencyAccountList.forEach { account ->
            totalBalance += currencyConverter.convertToUsd(account.value, account.currencyCode) ?: 0f
        }

        return totalBalance
    }
}
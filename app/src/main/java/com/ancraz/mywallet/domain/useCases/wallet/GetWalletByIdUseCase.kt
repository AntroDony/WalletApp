package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.domain.converter.CurrencyConverter
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWalletByIdUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    private val currencyConverter = CurrencyConverter(dataStoreRepository)

    suspend operator fun invoke(id: Long): DataResult<Wallet> {
        return try {
            repository.getWalletById(id).let { wallet ->
                DataResult.Success(
                    wallet.copy(
                        totalBalance = getTotalBalance(wallet.currencyAccountList)
                    )
                )
            }
        } catch (e: Exception) {
            debugLog("GetWalletByIdUseCase exception: ${e.message}")
            DataResult.Error("${e.message}")
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
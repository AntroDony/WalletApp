package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWalletByIdUseCase @Inject constructor(
    private val repository: WalletRepository
) {

    suspend operator fun invoke(id: Long): DataResult<Wallet> {
        return try {
            repository.getWalletById(id).let { wallet ->
                debugLog("GetWalletByIdUseCase : $wallet")
                DataResult.Success(wallet)
            }
        } catch (e: Exception) {
            debugLog("GetWalletByIdUseCase exception: ${e.message}")
            DataResult.Error("${e.message}")
        }
    }
}
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
){

    operator fun invoke(id: Long): Flow<DataResult<Wallet>> {
        return flow {
            try {
                emit(DataResult.Loading())

                repository.getWalletById(id).let { wallet ->
                    debugLog("GetWalletByIdUseCase : $wallet")
                    emit(DataResult.Success(wallet))
                }
            }
            catch (e: Exception){
                debugLog("GetWalletByIdUseCase exception: ${e.message}")
                emit(DataResult.Error("${e.message}"))
            }
        }

    }
}
package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllWalletsUseCase @Inject constructor(
    private val repository: WalletRepository
) {

    operator fun invoke(): Flow<DataResult<List<Wallet>>>{
        return flow {
            try {
                emit(DataResult.Loading())

                repository.getWalletList().collect{ wallets ->
                    debugLog("GetAllWalletsUseCase : $wallets")
                    emit(DataResult.Success(wallets))
                }
            } catch (e: Exception){
                debugLog("getAllWalletsUseCase exception: ${e.message}")
                emit(DataResult.Error("${e.message}"))
            }
        }
    }
}
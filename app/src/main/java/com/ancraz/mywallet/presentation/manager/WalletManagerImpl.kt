package com.ancraz.mywallet.presentation.manager

import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.manager.WalletManager
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.useCases.wallet.AddWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.DeleteWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetWalletByIdUseCase
import com.ancraz.mywallet.domain.useCases.wallet.UpdateWalletUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletManagerImpl @Inject constructor(
    private val getAllWalletsUseCase: GetAllWalletsUseCase,
    private val getWalletByIdUseCase: GetWalletByIdUseCase,
    private val addWalletUseCase: AddWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase
): WalletManager {

    override fun getWallets(): Flow<List<Wallet>> {
        return getAllWalletsUseCase()
    }

    override suspend fun getWalletById(id: Long): DataResult<Wallet> {
        return getWalletByIdUseCase(id)
    }

    override suspend fun addWallet(wallet: Wallet) {
        addWalletUseCase(wallet)
    }

    override suspend fun updateWallet(wallet: Wallet) {
        updateWalletUseCase(wallet)
    }

    override suspend fun deleteWalletById(id: Long) {
        deleteWalletUseCase(id)
    }

}
package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.WalletRepository
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {

    suspend operator fun invoke(wallet: Wallet){
        repository.updateWallet(wallet)
    }
}
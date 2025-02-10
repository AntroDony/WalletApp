package com.ancraz.mywallet.domain.useCases.wallet

import com.ancraz.mywallet.domain.repository.WalletRepository
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {

    suspend operator fun invoke(id: Long){
        repository.deleteWalletById(id)
    }
}
package com.ancraz.mywallet.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.ancraz.mywallet.domain.useCases.wallet.AddNewWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.DeleteWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetWalletByIdUseCase
import com.ancraz.mywallet.domain.useCases.wallet.UpdateWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletByIdUseCase: GetWalletByIdUseCase,
    private val addWalletUseCase: AddNewWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase
): ViewModel()  {


}
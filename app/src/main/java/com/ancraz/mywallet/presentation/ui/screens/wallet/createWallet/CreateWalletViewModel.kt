package com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.domain.useCases.wallet.AddWalletUseCase
import com.ancraz.mywallet.presentation.mapper.toWallet
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
    private val addWalletUseCase: AddWalletUseCase
): ViewModel()  {

    private val ioDispatcher = Dispatchers.IO

    private val _walletUiState =
        MutableStateFlow(WalletUiState())
    val walletUiState = _walletUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = WalletUiState()
    )

    fun addWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            addWalletUseCase(walletUi.toWallet())
        }
    }
}
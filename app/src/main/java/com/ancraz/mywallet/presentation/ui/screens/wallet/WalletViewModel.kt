package com.ancraz.mywallet.presentation.ui.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.WalletManager
import com.ancraz.mywallet.presentation.mapper.toWallet
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletManager: WalletManager
): ViewModel()  {

    private val ioDispatcher = Dispatchers.IO

    private val _walletListUiState = MutableStateFlow(WalletListUiState())
    val walletListUiState = _walletListUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = WalletListUiState()
    )

    private val _walletUiState = MutableStateFlow(WalletUiState())
    val walletUiState = _walletUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = WalletUiState()
    )

    init {
        fetchData()
    }

    fun resetWalletState(){
        _walletUiState.value = WalletUiState()
    }

    fun addWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            walletManager.addWallet(walletUi.toWallet())
        }
    }

    fun updateWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            walletManager.updateWallet(walletUi.toWallet())

            _walletUiState.value = _walletUiState.value.copy(
                wallet = null
            )
        }
    }

    fun deleteWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            walletManager.deleteWalletById(walletUi.id)
        }
    }

    fun getWalletById(id: Long){
        debugLog("viewModel: getWalletById")
        viewModelScope.launch(Dispatchers.IO) {
            walletManager.getWalletById(id).let{ result ->
                when(result){
                    is DataResult.Success -> {
                        _walletUiState.update {
                            it.copy(
                                isLoading = false,
                                wallet = result.data?.toWalletUi()
                            )
                        }
                    }
                    is DataResult.Loading -> {
                        _walletUiState.value = _walletUiState.value.copy(
                            isLoading = true
                        )
                    }
                    is DataResult.Error -> {
                        _walletUiState.value = _walletUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }
        }
    }


    private fun fetchData(){
        viewModelScope.launch(ioDispatcher) {
            walletManager.getWallets().collect { wallets ->
                _walletListUiState.update {
                    it.copy(
                        isLoading = false,
                        walletList = wallets.map { wallet ->
                            wallet.toWalletUi()
                        }
                    )
                }
            }
        }
    }
}
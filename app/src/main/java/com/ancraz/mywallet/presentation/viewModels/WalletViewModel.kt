package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.WalletManager
import com.ancraz.mywallet.presentation.mapper.toWallet
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletManager: WalletManager
): ViewModel()  {

    private val ioDispatcher = Dispatchers.IO

    private val _walletListUiState = mutableStateOf(WalletListUiState(isLoading = true))
    val walletListUiState: State<WalletListUiState> = _walletListUiState

    private val _walletUiState = mutableStateOf(WalletUiState(isLoading = true))
    val walletUiState: State<WalletUiState> = _walletUiState

    init {
        fetchData()
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
                        _walletUiState.value = _walletUiState.value.copy(
                            isLoading = false,
                            wallet = result.data?.toWalletUi()
                        )
                        cancel()
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
                _walletListUiState.value = _walletListUiState.value.copy(
                    isLoading = false,
                    walletList = wallets.map { wallet ->
                        wallet.toWalletUi()
                    }
                )
            }
        }
    }
}
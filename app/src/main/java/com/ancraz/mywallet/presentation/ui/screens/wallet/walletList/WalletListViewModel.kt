package com.ancraz.mywallet.presentation.ui.screens.wallet.walletList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllWalletsUseCase: GetAllWalletsUseCase
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _walletListUiState = MutableStateFlow(savedStateHandle[WALLET_LIST_SAVED_STATE_KEY] ?: WalletListUiState())
    val walletListUiState = _walletListUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = savedStateHandle[WALLET_LIST_SAVED_STATE_KEY] ?: WalletListUiState()
    )

    init {
        fetchData()
    }

    private fun fetchData(){
        viewModelScope.launch(ioDispatcher) {
            getAllWalletsUseCase().collect { wallets ->
                _walletListUiState.update {
                    it.copy(
                        isLoading = false,
                        walletList = wallets.map { wallet ->
                            wallet.toWalletUi()
                        }
                    )
                }
                updateWalletListSavedStateHandle()
            }
        }
    }

    private fun updateWalletListSavedStateHandle(){
        savedStateHandle[WALLET_LIST_SAVED_STATE_KEY] = _walletListUiState.value
    }


    companion object {
        private val WALLET_LIST_SAVED_STATE_KEY = "walletListUiState"
    }
}
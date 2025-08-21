package com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.useCases.wallet.DeleteWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetWalletByIdUseCase
import com.ancraz.mywallet.domain.useCases.wallet.UpdateWalletUseCase
import com.ancraz.mywallet.presentation.mapper.toWallet
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWalletByIdUseCase: GetWalletByIdUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _walletUiState = MutableStateFlow(savedStateHandle[WALLET_SAVED_STATE_KEY] ?: WalletUiState())
    val walletUiState = _walletUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = savedStateHandle[WALLET_SAVED_STATE_KEY] ?: WalletUiState()
    )

    fun getWalletById(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            getWalletByIdUseCase(id).let{ result ->
                when(result){
                    is DataResult.Success -> {
                        _walletUiState.update {
                            it.copy(
                                isLoading = false,
                                wallet = result.data?.toWalletUi()
                            )
                        }
                        savedStateHandle[WALLET_SAVED_STATE_KEY] = _walletUiState.value
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

    fun updateWallet(wallet: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            updateWalletUseCase(wallet.toWallet())

            //TODO refactor this code
            _walletUiState.value = _walletUiState.value.copy(
                wallet = null
            )

            updateWalletSavedStateHandle()
        }
    }

    private fun updateWalletSavedStateHandle(){
        savedStateHandle[WALLET_SAVED_STATE_KEY] = _walletUiState.value
    }

    fun deleteWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            deleteWalletUseCase(walletUi.id)
        }
    }

    companion object {
        private val WALLET_SAVED_STATE_KEY = "walletUiState"
    }
}
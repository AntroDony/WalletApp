package com.ancraz.mywallet.presentation.viewModels

import android.provider.ContactsContract.Data
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.useCases.wallet.AddNewWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.DeleteWalletUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetWalletByIdUseCase
import com.ancraz.mywallet.domain.useCases.wallet.UpdateWalletUseCase
import com.ancraz.mywallet.presentation.mapper.toWallet
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletsUseCase: GetAllWalletsUseCase,
    private val getWalletByIdUseCase: GetWalletByIdUseCase,
    private val addWalletUseCase: AddNewWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase
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
            addWalletUseCase(walletUi.toWallet())
        }
    }

    fun updateWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            updateWalletUseCase(walletUi.toWallet())

            _walletUiState.value = _walletUiState.value.copy(
                wallet = null
            )
        }
    }

    fun deleteWallet(walletUi: WalletUi){
        viewModelScope.launch(ioDispatcher) {
            deleteWalletUseCase(walletUi.id)
        }
    }

    fun getWallet(walletUi: WalletUi){
        viewModelScope.launch(Dispatchers.IO) {
            getWalletByIdUseCase(walletUi.id).onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _walletUiState.value = _walletUiState.value.copy(
                            isLoading = false,
                            wallet = result.data?.toWalletUi()
                        )
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
            }.launchIn(viewModelScope)
        }

    }


    private fun fetchData(){
        viewModelScope.launch {
            getWalletsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _walletListUiState.value = _walletListUiState.value.copy(
                            isLoading = false,
                            walletList = result.data?.map { wallet ->
                                wallet.toWalletUi()
                            } ?: emptyList()
                        )
                    }

                    is DataResult.Loading -> {
                        _walletListUiState.value = _walletListUiState.value.copy(
                            isLoading = true
                        )
                    }

                    is DataResult.Error -> {
                        _walletListUiState.value = _walletListUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
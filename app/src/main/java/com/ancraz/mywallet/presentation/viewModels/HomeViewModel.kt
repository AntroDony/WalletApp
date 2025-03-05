package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.ui.screens.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getAllWalletsUseCase: GetAllWalletsUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel(){

    private val ioDispatcher = Dispatchers.IO

    private var _homeUiState = mutableStateOf(HomeUiState())
    val homeUiState: State<HomeUiState> = _homeUiState

    init {
        fetchData()
    }

    fun editTotalBalance(value: Float, code: CurrencyCode = CurrencyCode.USD){
        viewModelScope.launch(ioDispatcher) {
            debugLog("updateTotalBalance")
            dataStoreManager.editTotalBalance(value, code)
        }
    }

    fun changePrivateMode(isPrivate: Boolean){
        viewModelScope.launch(ioDispatcher) {
            dataStoreManager.updatePrivateModeStatus(isPrivate)
        }
    }

    fun syncData(){
        viewModelScope.launch(ioDispatcher) {
            getAllWalletsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        val walletSumInUsd = result.data?.map { wallet ->
                            wallet.totalBalance
                        }?.sum()

                        walletSumInUsd?.let { value ->
                            dataStoreManager.editTotalBalance(value, CurrencyCode.USD)
                        } ?: run {
                            debugLog("walletSum is null")
                        }
                    }
                    is DataResult.Loading -> {
                        debugLog("syncData loading")
                    }
                    is DataResult.Error -> {
                        debugLog("syncData error: ${result.errorMessage}")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun fetchData(){
        viewModelScope.launch(ioDispatcher) {

//            getCurrencyRatesUseCase().onEach{ result ->
//                debugLog("GetCurrencyRateResult: ${result.data} | ${result.errorMessage}")
//            }.launchIn(viewModelScope)

            dataStoreManager.getTotalBalance().onEach{ result ->
                when(result){
                    is DataResult.Success -> {
                        _homeUiState.value = _homeUiState.value.copy(
                            isLoading = false,
                            data = _homeUiState.value.data.copy(balance = result.data ?: 0f)
                        )
                    }
                    is DataResult.Loading -> {
                        _homeUiState.value = _homeUiState.value.copy(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTotalBalance error: ${result.errorMessage}")
                        _homeUiState.value = _homeUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)

            dataStoreManager.getPrivateModeStatus().onEach { result ->
                _homeUiState.value = _homeUiState.value.copy(
                    data = _homeUiState.value.data.copy(isPrivateMode = result)
                )
            }.launchIn(viewModelScope)

            getAllTransactionsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _homeUiState.value = homeUiState.value.copy(
                            isLoading = false,
                            data = _homeUiState.value.data.copy(transactions = result.data?.map {
                                it.toTransactionUi()
                            }?.reversed() ?: emptyList())
                        )
                    }
                    is DataResult.Loading -> {
                        _homeUiState.value = _homeUiState.value.copy(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTransactions Error: ${result.errorMessage}")
                        _homeUiState.value = _homeUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)


            getAllWalletsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _homeUiState.value = homeUiState.value.copy(
                            data = _homeUiState.value.data.copy(
                                wallets = result.data?.map { wallet ->
                                    wallet.toWalletUi()
                                } ?: emptyList()
                            )
                        )
                    }
                    is DataResult.Loading -> {
                        debugLog("getWallet Loading")
                    }
                    is DataResult.Error -> {
                        debugLog("getWallet Error: ${result.errorMessage}")
                        _homeUiState.value = _homeUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
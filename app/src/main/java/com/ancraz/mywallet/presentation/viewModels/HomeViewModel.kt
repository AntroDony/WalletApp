package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.useCases.currency.GetCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.TotalBalanceUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.states.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val totalBalanceUseCase: TotalBalanceUseCase,
    private val getWalletsUseCase: GetAllWalletsUseCase
): ViewModel(){

    private val ioDispatcher = Dispatchers.IO

    private var _homeScreenState = mutableStateOf(HomeScreenState())
    val homeScreenState: State<HomeScreenState> = _homeScreenState


    init {
        fetchData()
    }


    fun editTotalBalance(value: Float, code: CurrencyCode = CurrencyCode.USD){
        viewModelScope.launch(ioDispatcher) {
            debugLog("updateTotalBalance")
            totalBalanceUseCase.editTotalBalance(value, code)
        }
    }

    private fun fetchData(){
        viewModelScope.launch(ioDispatcher) {
            getCurrencyRatesUseCase().onEach{ result ->
                debugLog("GetCurrencyRateResult: ${result.data} | ${result.errorMessage}")
            }.launchIn(viewModelScope)

            totalBalanceUseCase.getTotalBalanceFlow().onEach{ result ->
                when(result){
                    is DataResult.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isLoading = false,
                            data = _homeScreenState.value.data.copy(balance = result.data ?: 0f)
                        )
                    }
                    is DataResult.Loading -> {
                        _homeScreenState.value = _homeScreenState.value.copy(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTotalBalance error: ${result.errorMessage}")
                        _homeScreenState.value = _homeScreenState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)

            getTransactionsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _homeScreenState.value = homeScreenState.value.copy(
                            isLoading = false,
                            data = _homeScreenState.value.data.copy(transactions = result.data?.map {
                                it.toTransactionUi()
                            }?.reversed() ?: emptyList())
                        )
                    }
                    is DataResult.Loading -> {
                        _homeScreenState.value = _homeScreenState.value.copy(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTransactions Error: ${result.errorMessage}")
                        _homeScreenState.value = _homeScreenState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)


            getWalletsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _homeScreenState.value = homeScreenState.value.copy(
                            data = _homeScreenState.value.data.copy(

                            )
                        )
                    }
                    is DataResult.Loading -> {
                        debugLog("getWallet Loading")
                    }
                    is DataResult.Error -> {
                        debugLog("getWallet Error: ${result.errorMessage}")
                        _homeScreenState.value = _homeScreenState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }
        }
    }
}
package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.useCases.GetCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.TotalBalanceUseCase
import com.ancraz.mywallet.presentation.states.TotalBalanceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val totalBalanceUseCase: TotalBalanceUseCase
): ViewModel(){

    private val ioDispatcher = Dispatchers.IO

    private var _totalBalanceState = mutableStateOf(TotalBalanceState())
    val totalBalanceState: State<TotalBalanceState> = _totalBalanceState


    init {
        updateData()
    }


    fun updateTotalBalance(value: Float, code: CurrencyCode = CurrencyCode.USD){
        viewModelScope.launch(ioDispatcher) {
            debugLog("updateTotalBalance")
            totalBalanceUseCase.updateTotalBalance(value, code)
        }
    }

    private fun updateData(){
        viewModelScope.launch(ioDispatcher) {
            getCurrencyRatesUseCase.invoke().collect{ result ->
                debugLog("GetCurrencyRateResult: ${result.data} | ${result.errorMessage}")
            }

            totalBalanceUseCase.getTotalBalanceFlow().collect{ result ->
                when(result){
                    is DataResult.Success -> {
                        _totalBalanceState.value = TotalBalanceState(balance = result.data ?: 0f)
                    }
                    is DataResult.Loading -> {
                        _totalBalanceState.value = TotalBalanceState(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTotalBalance error: ${result.errorMessage}")
                        _totalBalanceState.value = TotalBalanceState(error = result.errorMessage)
                    }
                }
            }
        }
    }



}
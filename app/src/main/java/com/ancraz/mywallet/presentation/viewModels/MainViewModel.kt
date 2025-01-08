package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.useCases.GetCurrencyRatesUseCase
import com.ancraz.mywallet.presentation.states.CurrencyRateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
): ViewModel(){

    private val ioDispatcher = Dispatchers.IO

    private var _currenciesRateState = mutableStateOf(CurrencyRateState())
    val currenciesRateState: State<CurrencyRateState> = _currenciesRateState


    init {
        updateCurrenciesRates()
    }


    private fun updateCurrenciesRates(){
        viewModelScope.launch(ioDispatcher) {
            getCurrencyRatesUseCase.invoke().collect{ result ->
                debugLog("GetCurrencyRateResult: ${result.data} | ${result.errorMessage}")
            }
        }
    }
}
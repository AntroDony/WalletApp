package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.useCases.currency.GetCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transactions.DeleteTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionCategoriesUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.mapper.toCurrencyRateUi
import com.ancraz.mywallet.presentation.mapper.toTransaction
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.states.TransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionCategoriesUseCase: GetTransactionCategoriesUseCase,
    private val addTransactionCategoryUseCase: AddTransactionCategoryUseCase,
    private val deleteTransactionCategoryUseCase: DeleteTransactionCategoryUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _transactionUiState = mutableStateOf(TransactionUiState())
    val transactionUiState: State<TransactionUiState> = _transactionUiState

    init {
        getAllTransactionCategories()
        fetchCurrencyRates()
    }


    fun addNewTransaction(transactionUi: TransactionUi) {
        viewModelScope.launch(ioDispatcher) {
            debugLog("newTransaction: $transactionUi")
            addTransactionUseCase.addTransaction(transactionUi.toTransaction())
        }
    }


    private fun getAllTransactionCategories() {
        viewModelScope.launch(ioDispatcher) {
            getTransactionCategoriesUseCase.getExpenseCategories().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        _transactionUiState.value = _transactionUiState.value.copy(
                            isLoading = false,
                            data = _transactionUiState.value.data.copy(
                                expenseCategories = result.data?.map { category ->
                                    category.toCategoryUi()
                                } ?: emptyList()
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        _transactionUiState.value = TransactionUiState(isLoading = true)
                    }

                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _transactionUiState.value = TransactionUiState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)

            getTransactionCategoriesUseCase.getIncomeCategories().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        _transactionUiState.value = _transactionUiState.value.copy(
                            isLoading = false,
                            data = _transactionUiState.value.data.copy(
                                incomeCategories = result.data?.map { category ->
                                    category.toCategoryUi()
                                } ?: emptyList()
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        _transactionUiState.value = TransactionUiState(isLoading = true)
                    }

                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _transactionUiState.value = TransactionUiState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun fetchCurrencyRates() {
        viewModelScope.launch {
            getCurrencyRatesUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _transactionUiState.value = _transactionUiState.value.copy(
                            data = _transactionUiState.value.data.copy(
                                currencyRates = result.data?.map { rate ->
                                    rate.toCurrencyRateUi()
                                } ?: emptyList()
                            )
                        )
                    }
                    is DataResult.Loading -> {
                        debugLog("loading currency rates in transactionState")
                    }
                    is DataResult.Error -> {
                        debugLog("fetchCurrencyRates error: ${result.errorMessage}")
                        _transactionUiState.value = _transactionUiState.value.copy(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
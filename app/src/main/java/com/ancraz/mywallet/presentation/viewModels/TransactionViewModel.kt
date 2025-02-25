package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.useCases.currency.GetCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transactions.DeleteTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionCategoriesUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionsUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.mapper.toCurrencyRateUi
import com.ancraz.mywallet.presentation.mapper.toTransaction
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionUiState
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListUiState
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
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val addTransactionCategoryUseCase: AddTransactionCategoryUseCase,
    private val deleteTransactionCategoryUseCase: DeleteTransactionCategoryUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _createTransactionUiState = mutableStateOf(CreateTransactionUiState())
    val createTransactionUiState: State<CreateTransactionUiState> = _createTransactionUiState

    private var _transactionListUiState = mutableStateOf(TransactionListUiState())
    val transactionListUiState: State<TransactionListUiState> = _transactionListUiState


    init {
        fetchData()
    }


    fun addNewTransaction(transactionUi: TransactionUi) {
        viewModelScope.launch(ioDispatcher) {
            debugLog("newTransaction: $transactionUi")
            addTransactionUseCase.addTransaction(transactionUi.toTransaction())
        }
    }


    private fun fetchData(){
        viewModelScope.launch {
            fetchTransactionCategories()
            fetchTransactionList()
            fetchCurrencyRates()
        }
    }


    private fun fetchTransactionCategories() {
        viewModelScope.launch(ioDispatcher) {
            getTransactionCategoriesUseCase.getExpenseCategories().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(
                            isLoading = false,
                            data = _createTransactionUiState.value.data.copy(
                                expenseCategories = result.data?.map { category ->
                                    category.toCategoryUi()
                                } ?: emptyList()
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        _createTransactionUiState.value = CreateTransactionUiState(isLoading = true)
                    }

                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _createTransactionUiState.value = CreateTransactionUiState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)

            getTransactionCategoriesUseCase.getIncomeCategories().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(
                            isLoading = false,
                            data = _createTransactionUiState.value.data.copy(
                                incomeCategories = result.data?.map { category ->
                                    category.toCategoryUi()
                                } ?: emptyList()
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        _createTransactionUiState.value = CreateTransactionUiState(isLoading = true)
                    }

                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _createTransactionUiState.value = CreateTransactionUiState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun fetchTransactionList(){
        viewModelScope.launch {
            getTransactionsUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _transactionListUiState.value = _transactionListUiState.value.copy(
                            isLoading = false,
                            transactionList = result.data?.map { transaction: Transaction ->
                                transaction.toTransactionUi()
                            } ?: emptyList()
                        )
                    }
                    is DataResult.Loading -> {
                        _transactionListUiState.value = _transactionListUiState.value.copy(
                            isLoading = true
                        )
                    }
                    is DataResult.Error -> {
                        _transactionListUiState.value = _transactionListUiState.value.copy(
                            error = result.errorMessage
                        )
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
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(
                            data = _createTransactionUiState.value.data.copy(
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
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
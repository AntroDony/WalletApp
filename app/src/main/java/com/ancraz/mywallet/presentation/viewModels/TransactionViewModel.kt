package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.useCases.TotalBalanceUseCase
import com.ancraz.mywallet.domain.useCases.currency.GetCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transactions.DeleteTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.DeleteTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionByIdUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionCategoriesUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionsUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.mapper.toCurrencyRateUi
import com.ancraz.mywallet.presentation.mapper.toTransaction
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionUiState
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo.TransactionInfoUiState
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getTransactionCategoriesUseCase: GetTransactionCategoriesUseCase,
    private val addTransactionCategoryUseCase: AddTransactionCategoryUseCase,
    private val deleteTransactionCategoryUseCase: DeleteTransactionCategoryUseCase,
    private val totalBalanceUseCase: TotalBalanceUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _createTransactionUiState = mutableStateOf(CreateTransactionUiState())
    val createTransactionUiState: State<CreateTransactionUiState> = _createTransactionUiState

    private var _transactionListUiState = mutableStateOf(TransactionListUiState())
    val transactionListUiState: State<TransactionListUiState> = _transactionListUiState

    private var _transactionInfoUiState = mutableStateOf(TransactionInfoUiState())
    val transactionInfoUiState: State<TransactionInfoUiState> = _transactionInfoUiState


    init {
        fetchData()
    }


    fun addNewTransaction(transactionUi: TransactionUi) {
        viewModelScope.launch(ioDispatcher) {
            debugLog("newTransaction: $transactionUi")
            addTransactionUseCase.addTransaction(transactionUi.toTransaction())
        }
    }


    fun getTransactionById(id: Long){
        viewModelScope.launch(ioDispatcher) {
            debugLog("viewModel getTransactionById")
            getTransactionByIdUseCase(id).let { result ->
                when(result){
                    is DataResult.Success -> {
                        _transactionInfoUiState.value = _transactionInfoUiState.value.copy(
                            isLoading = false,
                            transaction = result.data?.toTransactionUi()
                        )
                        cancel()
                    }
                    is DataResult.Loading -> {
                        _transactionInfoUiState.value = _transactionInfoUiState.value.copy(
                            isLoading = true
                        )
                    }
                    is DataResult.Error -> {
                        _transactionInfoUiState.value = _transactionInfoUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }
        }
    }


    fun deleteTransactionById(id: Long){
        viewModelScope.launch(ioDispatcher) {
            deleteTransactionUseCase(id)
        }
    }


    private fun fetchData(){
        viewModelScope.launch(ioDispatcher) {
            fetchTotalBalance()
            fetchTransactionCategories()
            fetchTransactionList()
            fetchCurrencyRates()
        }
    }


    private fun fetchTotalBalance(){
        viewModelScope.launch(ioDispatcher) {
            totalBalanceUseCase.getTotalBalanceFlow().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(
                            isLoading = false,
                            data = _createTransactionUiState.value.data.copy(totalBalance = result.data ?: 0f)
                        )
                    }
                    is DataResult.Loading -> {
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getTotalBalance error: ${result.errorMessage}")
                        _createTransactionUiState.value = _createTransactionUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)
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
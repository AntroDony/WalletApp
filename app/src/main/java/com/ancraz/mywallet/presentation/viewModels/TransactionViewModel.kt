package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.manager.TransactionCategoryManager
import com.ancraz.mywallet.domain.manager.TransactionManager
import com.ancraz.mywallet.domain.manager.WalletManager
import com.ancraz.mywallet.domain.models.CurrencyRate
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.useCases.currency.GetCurrencyRatesUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.mapper.toCurrencyRateUi
import com.ancraz.mywallet.presentation.mapper.toTransaction
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionUiState
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo.TransactionInfoUiState
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val transactionManager: TransactionManager,
    private val transactionCategoryManager: TransactionCategoryManager,
    private val walletManager: WalletManager,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _createTransactionUiState = mutableStateOf(CreateTransactionUiState())
    val createTransactionUiState: State<CreateTransactionUiState> = _createTransactionUiState

    private var _transactionListUiState = mutableStateOf(TransactionListUiState())
    val transactionListUiState: State<TransactionListUiState> = _transactionListUiState

    private var _transactionInfoUiState = mutableStateOf(TransactionInfoUiState())
    val transactionInfoUiState: State<TransactionInfoUiState> = _transactionInfoUiState


    private val totalBalanceFlow: Flow<Float> = dataStoreManager.getTotalBalance()
    private val recentWalletIdFlow: Flow<Long> = dataStoreManager.getRecentWalletId()
    private val recentCurrencyFlow: Flow<CurrencyCode> = dataStoreManager.getRecentCurrency()
    private val incomeTransactionCategoriesFlow: Flow<List<TransactionCategory>> =
        transactionCategoryManager.getCategories(TransactionType.INCOME)
    private val expenseTransactionCategoriesFlow: Flow<List<TransactionCategory>> =
        transactionCategoryManager.getCategories(TransactionType.EXPENSE)
    private val transactionListFlow: Flow<List<Transaction>> = transactionManager.getTransactions()
    private val walletListFlow: Flow<List<Wallet>> = walletManager.getWallets()
    private val currencyRatesFlow: Flow<List<CurrencyRate>> = getCurrencyRatesUseCase()


    private var transactionList: List<TransactionUi> = emptyList()


    init {
        fetchData()
    }

    fun addNewTransaction(transactionUi: TransactionUi) {
        viewModelScope.launch(ioDispatcher) {
            transactionManager.addTransaction(transactionUi.toTransaction())

            //update dataStore values
            dataStoreManager.updateRecentCurrency(transactionUi.currency)
            transactionUi.wallet?.id?.let { id ->
                dataStoreManager.updateRecentWalletId(id)
            }
        }
    }

    fun getTransactionById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            transactionManager.getTransactionById(id).let { result ->
                when (result) {
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


    fun deleteTransactionById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            transactionManager.deleteTransaction(id)
        }
    }


    fun getTransactionsByType(type: TransactionType?) {
        viewModelScope.launch(Dispatchers.Default) {

            _transactionListUiState.value = _transactionListUiState.value.copy(
                transactionList = type?.let {
                    transactionList.filter { it.type == type }
                } ?: run {
                    transactionList
                }
            )
        }

    }


    private fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            try {
                combine(
                    totalBalanceFlow,
                    recentWalletIdFlow,
                    recentCurrencyFlow,
                    incomeTransactionCategoriesFlow,
                    expenseTransactionCategoriesFlow,
                    transactionListFlow,
                    walletListFlow,
                    currencyRatesFlow
                ) { values: Array<Any?> ->
                    debugLog("combine values")

                    val totalBalance = values[0] as Float
                    val recentWalletId = values[1] as Long
                    val recentCurrency = values[2] as CurrencyCode
                    val incomeCategories = values[3] as List<TransactionCategory>
                    val expenseCategories = values[4] as List<TransactionCategory>
                    val transactions = values[5] as List<Transaction>
                    val wallets = values[6] as List<Wallet>
                    val currencyRates = values[7] as List<CurrencyRate>

                    transactionList = transactions.map { it.toTransactionUi() }

                    CreateTransactionUiState(
                        isLoading = false,
                        data = CreateTransactionUiState.TransactionScreenData(
                            totalBalance = totalBalance,
                            incomeCategories = incomeCategories.map { it.toCategoryUi() },
                            expenseCategories = expenseCategories.map { it.toCategoryUi() },
                            currencyRates = currencyRates.map { it.toCurrencyRateUi() },
                            walletList = wallets.map { it.toWalletUi() },
                            recentWalletId = recentWalletId,
                            recentCurrency = recentCurrency
                        )
                    )
                }.collect {
                    debugLog("collect combine values")

                    _createTransactionUiState.value = it

                    _transactionListUiState.value = TransactionListUiState(
                        isLoading = false,
                        transactionList = transactionList
                    )

                    debugLog("_createTransactionUiState: ${_createTransactionUiState.value.data}")
                }
            } catch (e: Exception) {
                debugLog("fetchData exception: ${e.message}")

                _createTransactionUiState.value = _createTransactionUiState.value.copy(
                    error = e.message
                )
            }

        }
    }
}
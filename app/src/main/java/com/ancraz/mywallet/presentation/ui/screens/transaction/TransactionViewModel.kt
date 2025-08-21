package com.ancraz.mywallet.presentation.ui.screens.transaction

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
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private var _createTransactionUiState = MutableStateFlow(CreateTransactionUiState())
    val createTransactionUiState: StateFlow<CreateTransactionUiState> =
        _createTransactionUiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = CreateTransactionUiState()
        )


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
                    val totalBalance = values[0] as Float
                    val recentWalletId = values[1] as Long
                    val recentCurrency = values[2] as CurrencyCode
                    val incomeCategories = values[3] as List<TransactionCategory>
                    val expenseCategories = values[4] as List<TransactionCategory>
                    val transactions = values[5] as List<Transaction>
                    val wallets = values[6] as List<Wallet>
                    val currencyRates = values[7] as List<CurrencyRate>

                    debugLog("currencyRates: $currencyRates")
                    debugLog("recentWalletId: $recentWalletId")
                    debugLog("recentCurrency: $recentCurrency")

                    transactionList = transactions.map { it.toTransactionUi() }

                    CreateTransactionUiState(
                        isLoading = false,
                        data = CreateTransactionUiState.TransactionScreenData(
                            totalBalance = totalBalance.toFormattedString(),
                            incomeCategories = incomeCategories.map { it.toCategoryUi() },
                            expenseCategories = expenseCategories.map { it.toCategoryUi() },
                            currencyRates = currencyRates.map { it.toCurrencyRateUi() },
                            walletList = wallets.map { it.toWalletUi() },
                            recentWalletId = recentWalletId,
                            recentCurrency = recentCurrency
                        )
                    )
                }.collect {
                    _createTransactionUiState.value = it
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
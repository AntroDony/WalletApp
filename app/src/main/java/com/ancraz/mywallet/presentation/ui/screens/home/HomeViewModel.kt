package com.ancraz.mywallet.presentation.ui.screens.home

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.useCases.currency.UpdateCurrencyRatesUseCase
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.ui.widget.WalletWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getAllWalletsUseCase: GetAllWalletsUseCase,
    private val updateCurrencyRatesUseCase: UpdateCurrencyRatesUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _homeUiState = MutableStateFlow(savedStateHandle[UI_SAVED_STATE_KEY] ?: HomeUiState())
    val homeUiState = _homeUiState.stateIn(
        viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = savedStateHandle[UI_SAVED_STATE_KEY] ?: HomeUiState()
    )

    private val privateModeStatusFlow = dataStoreManager.getPrivateModeStatus()
    private val totalBalanceFlow = dataStoreManager.getTotalBalance()
    private val walletListFlow: Flow<List<Wallet>> = getAllWalletsUseCase()
    private val transactionListFlow: Flow<List<Transaction>> = getAllTransactionsUseCase()
    private val currencyRatesUpdateResult: Flow<DataResult<String?>> = updateCurrencyRatesUseCase()

    init {
        fetchData()
    }

    fun editTotalBalance(value: Float, code: CurrencyCode = CurrencyCode.USD) {
        viewModelScope.launch(ioDispatcher) {
            dataStoreManager.editTotalBalance(value, code)
        }
    }

    fun changePrivateMode(isPrivate: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            dataStoreManager.updatePrivateModeStatus(isPrivate)
        }
    }

    fun syncData() {
        viewModelScope.launch(ioDispatcher) {
            getAllWalletsUseCase().collect { walletList ->
                val walletSumInUsd = walletList.map { wallet ->
                    wallet.totalBalance
                }.sum()

                walletSumInUsd.let { value ->
                    dataStoreManager.editTotalBalance(value, CurrencyCode.USD)
                }
            }
        }
    }

    private fun fetchData() {
        syncDataAfterWalletListUpdated()
        viewModelScope.launch(ioDispatcher) {
            try {
                combine(
                    privateModeStatusFlow,
                    totalBalanceFlow,
                    walletListFlow,
                    transactionListFlow,
                    currencyRatesUpdateResult
                ) { privateModeStatus, totalBalance, walletList, transactionList, currencyRatesResult ->
                    HomeUiState(
                        isLoading = false,
                        data = HomeUiState.HomeScreenData(
                            isPrivateMode = privateModeStatus,
                            balance = totalBalance,
                            wallets = walletList.map { it.toWalletUi() },
                            transactions = transactionList.map { it.toTransactionUi() }
                        ),
                        error = when(currencyRatesResult){
                            is DataResult.Error<*> -> {
                                currencyRatesResult.errorMessage
                            }
                            else -> null
                        }
                    )
                }.collect { uiState ->
                    _homeUiState.value = uiState
                    updateSavedStateHandle()

                    updateWidgetState(
                        balance = uiState.data.balance,
                        isPrivateMode = uiState.data.isPrivateMode
                    )
                }
            } catch (e: Exception){
                debugLog("fetchData exception: ${e.message}")
                _homeUiState.value = _homeUiState.value.copy(
                    error = e.message
                )
                updateSavedStateHandle()
            }
        }
    }

    private fun updateSavedStateHandle(){
        savedStateHandle[UI_SAVED_STATE_KEY] = _homeUiState.value
    }


    private fun syncDataAfterWalletListUpdated(){
        viewModelScope.launch(Dispatchers.IO) {
            walletListFlow.collect {
                syncData()
            }
        }
    }


    //TODO move to WidgetManager class
    private suspend fun updateWidgetState(balance: Float, isPrivateMode: Boolean) {
        try {
            val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(WalletWidget::class.java)

            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[WalletWidget.totalBalanceKey] = balance
                    prefs[WalletWidget.isPrivateModeKey] = isPrivateMode
                }

                WalletWidget.update(context, glanceId)
            }
        } catch (e: Exception) {
            debugLog("updateWidgetState exception: ${e.message}")
        }
    }


    companion object {
        private const val UI_SAVED_STATE_KEY = "homeUiState"
    }
}
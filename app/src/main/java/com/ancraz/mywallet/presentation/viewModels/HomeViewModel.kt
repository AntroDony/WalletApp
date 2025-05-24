package com.ancraz.mywallet.presentation.viewModels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.manager.DataStoreManager
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.wallet.GetAllWalletsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.mapper.toWalletUi
import com.ancraz.mywallet.presentation.ui.screens.home.HomeUiState
import com.ancraz.mywallet.presentation.ui.widget.WalletWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getAllWalletsUseCase: GetAllWalletsUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _homeUiState = mutableStateOf(HomeUiState())
    val homeUiState: State<HomeUiState> = _homeUiState

    private val privateModeStatusFlow = dataStoreManager.getPrivateModeStatus()
    private val totalBalanceFlow = dataStoreManager.getTotalBalance()
    private val walletListFlow: Flow<DataResult<List<Wallet>>> = getAllWalletsUseCase()
    private val transactionListFlow: Flow<DataResult<List<Transaction>>> = getAllTransactionsUseCase()

    init {
        fetchData()
    }

    fun editTotalBalance(value: Float, code: CurrencyCode = CurrencyCode.USD) {
        viewModelScope.launch(ioDispatcher) {
            debugLog("updateTotalBalance")
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
            getAllWalletsUseCase().collect { result ->
                when (result) {
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
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            try {
                combine(
                    privateModeStatusFlow,
                    totalBalanceFlow,
                    walletListFlow,
                    transactionListFlow
                ) { privateModeStatus, totalBalanceDataResult, walletListDataResult, transactionListDataResult ->
                    if (totalBalanceDataResult is DataResult.Error
                        || walletListDataResult is DataResult.Error
                        || transactionListDataResult is DataResult.Error
                    ) {                        _homeUiState.value = _homeUiState.value.copy(
                            error = "${totalBalanceDataResult.errorMessage} | " +
                                    "${walletListDataResult.errorMessage} | " +
                                    "${transactionListDataResult.errorMessage}"
                        )
                    }

                    HomeUiState(
                        isLoading = false,
                        data = HomeUiState.HomeScreenData(
                            isPrivateMode = privateModeStatus,
                            balance = totalBalanceDataResult.data ?: 0f,
                            wallets = walletListDataResult.data?.map { it.toWalletUi() } ?: emptyList(),
                            transactions = transactionListDataResult.data?.map { it.toTransactionUi() } ?: emptyList()
                        )
                    )
                }.collect {
                    _homeUiState.value = it

                    updateWidgetState(
                        balance = it.data.balance,
                        isPrivateMode = it.data.isPrivateMode
                    )
                }
            } catch (e: Exception){
                debugLog("fetchData exception: ${e.message}")
                _homeUiState.value = _homeUiState.value.copy(
                    error = e.message
                )
            }
        }
    }


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
}
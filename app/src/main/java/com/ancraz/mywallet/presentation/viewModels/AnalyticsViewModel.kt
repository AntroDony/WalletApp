package com.ancraz.mywallet.presentation.viewModels

import android.provider.ContactsContract.Data
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.ui.screens.analytics.AnalyticsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _analyticsUiState = mutableStateOf(AnalyticsUiState())
    val analyticsUiState: State<AnalyticsUiState> = _analyticsUiState


    init {
        fetchData()
    }


    fun filterAnalyticsByPeriod(period: AnalyticsPeriod) {
//        _analyticsUiState.value = _analyticsUiState.value.copy(
//            data = _analyticsUiState.value.data.copy(
//                fil
//            )
//        )
    }


    fun filterAnalyticsByTransactionType(transactionType: TransactionType?) {
        _analyticsUiState.value = _analyticsUiState.value.copy(
            data = _analyticsUiState.value.data.copy(
                filteredTransactionList = if (transactionType == null) {
                    _analyticsUiState.value.data.transactionList
                } else {
                    _analyticsUiState.value.data.transactionList.filter { transaction ->
                        transaction.type == transactionType
                    }
                }
            )
        )
    }


    private fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            getAllTransactionsUseCase().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        _analyticsUiState.value = _analyticsUiState.value.copy(
                            data = _analyticsUiState.value.data.copy(
                                transactionList = result.data?.map { transaction: Transaction ->
                                    transaction.toTransactionUi()
                                } ?: emptyList(),
                                filteredTransactionList = result.data?.map { transaction: Transaction ->
                                    transaction.toTransactionUi()
                                } ?: emptyList()
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        debugLog("getTransaction Loading")
                    }

                    is DataResult.Error -> {
                        _analyticsUiState.value = _analyticsUiState.value.copy(
                            error = result.errorMessage
                        )
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
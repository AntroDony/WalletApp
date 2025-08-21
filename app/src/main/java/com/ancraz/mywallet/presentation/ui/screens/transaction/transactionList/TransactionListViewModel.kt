package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
): ViewModel(){

    private var _transactionListUiState = MutableStateFlow(savedStateHandle[TRANSACTION_LIST_UI_STATE] ?: TransactionListUiState())
    val transactionListUiState = _transactionListUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = savedStateHandle[TRANSACTION_LIST_UI_STATE] ?: TransactionListUiState()
    )
    private var allTransactionList: List<TransactionUi> = emptyList()

    init {
        fetchData()
    }

    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllTransactionsUseCase().collect { transactions ->
                allTransactionList = transactions.map {
                    it.toTransactionUi()
                }

                _transactionListUiState.update {
                    it.copy(
                        isLoading = false,
                        transactionList = allTransactionList,
                    )
                }
            }
        }
    }


    fun filterTransactionsByType(type: TransactionType?) {
        viewModelScope.launch(Dispatchers.Default) {

            _transactionListUiState.update { uiState ->
                uiState.copy(
                    transactionList = type?.let {
                        allTransactionList.filter { it.type == type }
                    } ?: run {
                        allTransactionList
                    },
                    filteredType = type
                )
            }
            savedStateHandle[TRANSACTION_LIST_UI_STATE] = _transactionListUiState.value
        }
    }

    companion object {
        private const val TRANSACTION_LIST_UI_STATE = "transactionListUiState"
    }
}
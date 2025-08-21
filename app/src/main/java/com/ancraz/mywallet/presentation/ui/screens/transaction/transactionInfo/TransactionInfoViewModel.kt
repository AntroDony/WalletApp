package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.domain.useCases.transaction.DeleteTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transaction.GetTransactionByIdUseCase
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionInfoViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _transactionInfoUiState = MutableStateFlow(TransactionInfoUiState())
    val transactionInfoUiState = _transactionInfoUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = TransactionInfoUiState()
    )

    fun getTransactionById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            getTransactionByIdUseCase(id).let { result ->
                when (result) {
                    is DataResult.Success -> {
                        _transactionInfoUiState.update {
                            it.copy(
                                isLoading = false,
                                transaction = result.data?.toTransactionUi()
                            )
                        }
                    }

                    is DataResult.Loading -> {
                        _transactionInfoUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is DataResult.Error -> {
                        _transactionInfoUiState.update {
                            it.copy(
                                error = result.errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteTransactionById(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            deleteTransactionUseCase(id)
        }
    }
}
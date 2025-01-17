package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.AddTransactionUseCase
import com.ancraz.mywallet.domain.useCases.transactions.DeleteTransactionCategoryUseCase
import com.ancraz.mywallet.domain.useCases.transactions.GetTransactionCategoriesUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.states.TransactionCategoriesState
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
    private val deleteTransactionCategoryUseCase: DeleteTransactionCategoryUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var _transactionCategoriesState = mutableStateOf(TransactionCategoriesState())
    val transactionCategoriesState: State<TransactionCategoriesState> = _transactionCategoriesState

    init {
        getAllTransactionCategories()
    }


    private fun getAllTransactionCategories(){
        viewModelScope.launch(ioDispatcher) {
            getTransactionCategoriesUseCase.getExpenseCategories().onEach{ result ->
                when(result){
                    is DataResult.Success -> {
                        _transactionCategoriesState.value = _transactionCategoriesState.value.copy(
                            expenseCategories = result.data?.map { category ->
                                category.toCategoryUi()
                            } ?: emptyList()
                        )
                    }
                    is  DataResult.Loading -> {
                        _transactionCategoriesState.value = TransactionCategoriesState(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _transactionCategoriesState.value = TransactionCategoriesState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)

            getTransactionCategoriesUseCase.getIncomeCategories().onEach{ result ->
                when(result){
                    is DataResult.Success -> {
                        _transactionCategoriesState.value = _transactionCategoriesState.value.copy(
                            incomeCategories = result.data?.map { category ->
                                category.toCategoryUi()
                            } ?: emptyList()
                        )
                    }
                    is  DataResult.Loading -> {
                        _transactionCategoriesState.value = TransactionCategoriesState(isLoading = true)
                    }
                    is DataResult.Error -> {
                        debugLog("getExpenseCategories error: ${result.errorMessage}")
                        _transactionCategoriesState.value = TransactionCategoriesState(error = result.errorMessage)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


}
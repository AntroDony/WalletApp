package com.ancraz.mywallet.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.result.DataResult
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.useCases.analytics.GetExpenseSumUseCase
import com.ancraz.mywallet.domain.useCases.analytics.GetIncomeSumUseCase
import com.ancraz.mywallet.domain.useCases.analytics.GetTransactionsByPeriodUseCase
import com.ancraz.mywallet.domain.useCases.transaction.GetAllTransactionsUseCase
import com.ancraz.mywallet.domain.useCases.transactionCategory.GetTransactionCategoriesUseCase
import com.ancraz.mywallet.presentation.mapper.toCategoryUi
import com.ancraz.mywallet.presentation.mapper.toTransaction
import com.ancraz.mywallet.presentation.mapper.toTransactionUi
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.screens.analytics.AnalyticsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getIncomeSumUseCase: GetIncomeSumUseCase,
    private val getExpenseSumUseCase: GetExpenseSumUseCase,
    private val getTransactionCategoriesUseCase: GetTransactionCategoriesUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _analyticsUiState = mutableStateOf(AnalyticsUiState())
    val analyticsUiState: State<AnalyticsUiState> = _analyticsUiState

    private var transactionList = emptyList<TransactionUi>()

    init {
        fetchData()
    }


    fun filterAnalyticsData(
        transactionType: TransactionType?,
        transactionCategory: TransactionCategoryUi?,
        period: AnalyticsPeriod,
        periodOffset: Int
    ){
        viewModelScope.launch(ioDispatcher) {

            debugLog("filterAnalytics: \n" +
                    "type: $transactionType\n" +
                    "category: $transactionCategory\n" +
                    "period: $period\n" +
                    "offset: $periodOffset")

            val filteredByCategoryTransactionList = transactionCategory?.let { category ->
                transactionList.filter { it.category == category }
            } ?: transactionList


            debugLog("filteredByCategoryTransactions: $filteredByCategoryTransactionList")

            getTransactionsByPeriodUseCase(
                period = period,
                offset = periodOffset,
                transactionList = filteredByCategoryTransactionList.map { it.toTransaction() }
            ).let { result ->
                debugLog("periodFilterResult: ${result.size}")

                val incomeSum = getIncomeSumUseCase(result)
                val expenseSum = getExpenseSumUseCase(result)
                val resultTransactionList = if (transactionType == null) {
                    result.map { it.toTransactionUi() }
                } else {
                    result.map {
                        it.toTransactionUi()
                    }.filter {
                        it.type == transactionType
                    }
                }

                debugLog("filterResult:\n" +
                        "transactions: $resultTransactionList")

                _analyticsUiState.value = _analyticsUiState.value.copy(
                    data = _analyticsUiState.value.data.copy(
                        totalBalanceInUsd = incomeSum - expenseSum,
                        incomeValueInUsd = incomeSum,
                        expenseValueInUsd = expenseSum,
                        filteredTransactionList = resultTransactionList
                    )
                )
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch(ioDispatcher) {
            getAllTransactionsUseCase().onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        transactionList = result.data?.map { transaction: Transaction ->
                                transaction.toTransactionUi()
                        } ?: emptyList()

                        _analyticsUiState.value = _analyticsUiState.value.copy(
                            data = _analyticsUiState.value.data.copy(
                                filteredTransactionList = transactionList
                            )
                        )
                        filterAnalyticsData(
                            transactionType = null,
                            transactionCategory = null,
                            period = AnalyticsPeriod.Day,
                            periodOffset = 0
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


            getTransactionCategoriesUseCase().onEach { result ->
                when(result){
                    is DataResult.Success -> {
                        _analyticsUiState.value = _analyticsUiState.value.copy(
                            data = _analyticsUiState.value.data.copy(
                                transactionCategoryList = result.data?.map { categoryList: TransactionCategory ->
                                    categoryList.toCategoryUi()
                                } ?: emptyList()
                            )
                        )
                    }
                    is DataResult.Loading -> {
                        debugLog("getCategories Loading")
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
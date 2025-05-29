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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    private val transactionCategoryListFlow: Flow<List<TransactionCategory>> = getTransactionCategoriesUseCase()
    private val allTransactionsListFlow: Flow<List<Transaction>> = getAllTransactionsUseCase()

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

            getTransactionsByPeriodUseCase(
                period = period,
                offset = periodOffset,
                transactionList = filteredByCategoryTransactionList.map { it.toTransaction() }
            ).let { result ->
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
            try {
                combine(
                    allTransactionsListFlow,
                    transactionCategoryListFlow
                ) { allTransactionsList, categoryList ->
                    transactionList = allTransactionsList.map { it.toTransactionUi() }
                    filterAnalyticsData(
                        transactionType = null,
                        transactionCategory = null,
                        period = AnalyticsPeriod.Day,
                        periodOffset = 0
                    )


                    AnalyticsUiState(
                        isLoading = false,
                        data = AnalyticsUiState.AnalyticsScreenData(
                            filteredTransactionList = transactionList,
                            transactionCategoryList = categoryList.map { it.toCategoryUi() }
                        )
                    )
                }.collect { uiState ->
                    _analyticsUiState.value = uiState
                }
            } catch (e: Exception) {
                _analyticsUiState.value = _analyticsUiState.value.copy(
                    error = e.message
                )
            }
        }
    }
}
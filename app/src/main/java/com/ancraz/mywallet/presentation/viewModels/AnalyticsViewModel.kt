package com.ancraz.mywallet.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.domain.models.Transaction
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
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getIncomeSumUseCase: GetIncomeSumUseCase,
    private val getExpenseSumUseCase: GetExpenseSumUseCase,
    private val getTransactionCategoriesUseCase: GetTransactionCategoriesUseCase
) : ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _analyticsUiState =
        MutableStateFlow(savedStateHandle[UI_SAVED_STATE_KEY] ?: AnalyticsUiState())
    val analyticsUiState = _analyticsUiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = savedStateHandle[UI_SAVED_STATE_KEY] ?: AnalyticsUiState()
    )

    private val transactionCategoryListFlow: Flow<List<TransactionCategoryUi>> =
        getTransactionCategoriesUseCase()
            .map { list ->
                list.map { category ->
                    category.toCategoryUi()
                }
            }
    private val allTransactionsListFlow: Flow<List<Transaction>> = getAllTransactionsUseCase()

    private var transactionList = emptyList<TransactionUi>()

    init {
        fetchData()
    }

    fun filterAnalyticsByPeriod(
        period: AnalyticsPeriod
    ) {
        filterAnalyticsData(
            period = period,
            periodOffset = 0
        )
    }

    fun filterAnalyticsByPeriodOffset(
        periodOffset: Int
    ) {
        filterAnalyticsData(
            periodOffset = periodOffset
        )
    }

    fun filterAnalyticsByTransactionType(
        type: TransactionType?
    ) {
        filterAnalyticsData(transactionType = type)
    }

    fun filterAnalyticsByCategory(
        category: TransactionCategoryUi?
    ) {
        filterAnalyticsData(transactionCategory = category)
    }

    private fun filterAnalyticsData(
        transactionType: TransactionType? = _analyticsUiState.value.data.transactionType,
        transactionCategory: TransactionCategoryUi? = _analyticsUiState.value.data.transactionCategory,
        period: AnalyticsPeriod = _analyticsUiState.value.data.period,
        periodOffset: Int = _analyticsUiState.value.data.periodOffset
    ) {
        viewModelScope.launch(ioDispatcher) {

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

                _analyticsUiState.update {
                    savedStateHandle[UI_SAVED_STATE_KEY] = _analyticsUiState.value
                    it.copy(
                        data = _analyticsUiState.value.data.copy(
                            totalBalanceInUsd = (incomeSum - expenseSum).toFormattedUsdString(),
                            incomeValueInUsd = incomeSum.toFormattedUsdString(),
                            expenseValueInUsd = expenseSum.toFormattedUsdString(),
                            filteredTransactionList = resultTransactionList,
                            period = period,
                            transactionCategory = transactionCategory,
                            transactionType = transactionType,
                            periodOffset = periodOffset
                        )
                    )
                }
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

                    _analyticsUiState.value.data.apply {
                        filterAnalyticsData(
                            transactionType = transactionType,
                            transactionCategory = transactionCategory,
                            period = period,
                            periodOffset = periodOffset
                        )
                    }

                    AnalyticsUiState(
                        isLoading = false,
                        data = AnalyticsUiState.AnalyticsScreenData(
                            filteredTransactionList = transactionList,
                            transactionCategoryList = categoryList
                        )
                    )
                }.collect { uiState ->
                    _analyticsUiState.update {
                        savedStateHandle[UI_SAVED_STATE_KEY] = _analyticsUiState.value
                        it.copy(
                            isLoading = uiState.isLoading,
                            data = uiState.data
                        )
                    }
                }
            } catch (e: Exception) {
                _analyticsUiState.update {
                    savedStateHandle[UI_SAVED_STATE_KEY] = _analyticsUiState.value
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }

    private fun Float.toFormattedUsdString(): String {
        return "$ ${this.toFormattedString()}"
    }

    companion object {

        private val UI_SAVED_STATE_KEY = "uiState"
    }
}
package com.ancraz.mywallet.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.ui.events.AnalyticsUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateWalletUiEvent
import com.ancraz.mywallet.presentation.ui.events.EditBalanceUiEvent
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionListUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletListUiEvent
import com.ancraz.mywallet.presentation.ui.screens.analytics.AnalyticsScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceUiState
import com.ancraz.mywallet.presentation.ui.screens.home.HomeScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo.TransactionInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet.CreateWalletScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo.WalletInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListScreen
import com.ancraz.mywallet.presentation.viewModels.AnalyticsViewModel
import com.ancraz.mywallet.presentation.viewModels.HomeViewModel
import com.ancraz.mywallet.presentation.viewModels.TransactionViewModel
import com.ancraz.mywallet.presentation.viewModels.WalletViewModel

@Composable
fun AppNavigation(
    startDestination: NavigationRoute,
    innerPadding: PaddingValues
) {

    val backStack = rememberNavBackStack(startDestination)

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<NavigationRoute.HomeScreen> {
                val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()

                HomeScreen(
                    uiState = homeViewModel.homeUiState.collectAsStateWithLifecycle().value,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event ->
                        when (event) {
                            is HomeUiEvent.CreateTransaction -> {
                                backStack.add(
                                    NavigationRoute.TransactionInputScreen(event.transactionType)
                                )
                            }

                            is HomeUiEvent.EditTotalBalance -> {
                                backStack.add(
                                    NavigationRoute.EditBalanceScreen(event.currentBalance)
                                )
                            }

                            is HomeUiEvent.ShowAllWallets -> {
                                backStack.add(
                                    NavigationRoute.WalletListScreen
                                )
                            }

                            is HomeUiEvent.ShowAllTransactions -> {
                                backStack.add(
                                    NavigationRoute.TransactionListScreen
                                )
                            }

                            is HomeUiEvent.ShowAnalytics -> {
                                backStack.add(
                                    NavigationRoute.AnalyticsScreen
                                )
                            }

                            is HomeUiEvent.ShowWalletInfo -> {
                                backStack.add(
                                    NavigationRoute.WalletInfoScreen(event.wallet.id)
                                )
                            }

                            is HomeUiEvent.ShowTransactionInfo -> {
                                backStack.add(
                                    NavigationRoute.TransactionInfoScreen(event.transaction.id)
                                )
                            }

                            is HomeUiEvent.CreateWallet -> {
                                backStack.add(
                                    NavigationRoute.CreateWalletScreen
                                )
                            }

                            is HomeUiEvent.SyncData -> {
                                homeViewModel.syncData()
                            }

                            is HomeUiEvent.ChangePrivateMode -> {
                                homeViewModel.changePrivateMode(event.isPrivate)
                            }
                        }
                    },
                )
            }

            entry<NavigationRoute.EditBalanceScreen> { key ->
                val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
                val currentBalance = key.totalBalance

                EditBalanceScreen(
                    uiState = EditBalanceUiState(
                        data = EditBalanceUiState.EditBalanceScreenData(
                            currentTotalBalance = currentBalance
                        )
                    ),
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event: EditBalanceUiEvent ->
                        when (event) {
                            is EditBalanceUiEvent.UpdateBalanceValue -> {
                                homeViewModel.editTotalBalance(event.newBalance)
                            }

                            is EditBalanceUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionInputScreen> { key ->
                val transactionViewModel: TransactionViewModel =
                    hiltViewModel<TransactionViewModel>()

                val transactionType = key.transactionType

                CreateTransactionScreen(
                    uiState = transactionViewModel.createTransactionUiState.collectAsStateWithLifecycle().value,
                    transactionType = transactionType,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event: CreateTransactionUiEvent ->
                        when (event) {
                            is CreateTransactionUiEvent.AddTransaction -> {
                                transactionViewModel.addNewTransaction(event.transaction)
                                backStack.apply {
                                    if (this.toList().size == 1) {
                                        add(
                                            NavigationRoute.HomeScreen
                                        )
                                        remove(key)
                                    } else {
                                        removeLastOrNull()
                                    }

                                }
                            }

                            is CreateTransactionUiEvent.CreateWallet -> {
                                backStack.add(
                                    NavigationRoute.CreateWalletScreen
                                )
                            }

                            is CreateTransactionUiEvent.GoBack -> {
                                backStack.apply {
                                    if (this.toList().size == 1) {
                                        add(
                                            NavigationRoute.HomeScreen
                                        )
                                        remove(key)
                                    } else {
                                        removeLastOrNull()
                                    }
                                }
                            }
                        }
                    },
                )
            }

            entry<NavigationRoute.WalletListScreen> {
                val walletViewModel: WalletViewModel = hiltViewModel<WalletViewModel>()

                WalletListScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    uiState = walletViewModel.walletListUiState.collectAsStateWithLifecycle().value,
                    onEvent = { event: WalletListUiEvent ->
                        when (event) {
                            is WalletListUiEvent.ShowWalletInfo -> {
                                backStack.add(
                                    NavigationRoute.WalletInfoScreen(event.wallet.id)
                                )
                            }

                            is WalletListUiEvent.CreateWallet -> {
                                walletViewModel.resetWalletState()
                                backStack.add(
                                    NavigationRoute.CreateWalletScreen
                                )
                            }

                            is WalletListUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.CreateWalletScreen> {
                val walletViewModel: WalletViewModel = hiltViewModel<WalletViewModel>()

                CreateWalletScreen(
                    uiState = walletViewModel.walletUiState.collectAsStateWithLifecycle().value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: CreateWalletUiEvent ->
                        when (event) {
                            is CreateWalletUiEvent.AddWallet -> {
                                walletViewModel.addWallet(event.wallet)
                            }

                            is CreateWalletUiEvent.UpdateWallet -> {
                                walletViewModel.updateWallet(event.wallet)
                            }

                            is CreateWalletUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.WalletInfoScreen> { key ->
                val walletViewModel: WalletViewModel = hiltViewModel<WalletViewModel>()
                walletViewModel.getWalletById(key.walletId)

                WalletInfoScreen(
                    uiState = walletViewModel.walletUiState.collectAsStateWithLifecycle().value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: WalletInfoUiEvent ->
                        when (event) {
                            is WalletInfoUiEvent.EditWallet -> {
                                backStack.add(
                                    NavigationRoute.CreateWalletScreen
                                )
                            }

                            is WalletInfoUiEvent.DeleteWallet -> {
                                walletViewModel.deleteWallet(event.wallet)
                                backStack.removeLastOrNull()
                            }

                            is WalletInfoUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionListScreen> {
                val transactionViewModel: TransactionViewModel =
                    hiltViewModel<TransactionViewModel>()

                TransactionListScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    uiState = transactionViewModel.transactionListUiState.collectAsStateWithLifecycle().value,
                    onEvent = { event: TransactionListUiEvent ->
                        when (event) {
                            is TransactionListUiEvent.ShowTransactionInfo -> {
                                backStack.add(
                                    NavigationRoute.TransactionInfoScreen(event.transaction.id)
                                )
                            }

                            is TransactionListUiEvent.GetTransactionsByType -> {
                                transactionViewModel.getTransactionsByType(event.transactionType)
                            }

                            is TransactionListUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionInfoScreen> { key ->
                val transactionViewModel: TransactionViewModel =
                    hiltViewModel<TransactionViewModel>()
                transactionViewModel.getTransactionById(key.transactionId)

                TransactionInfoScreen(
                    uiState = transactionViewModel.transactionInfoUiState.collectAsStateWithLifecycle().value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: TransactionInfoUiEvent ->
                        when (event) {
                            is TransactionInfoUiEvent.DeleteTransaction -> {
                                transactionViewModel.deleteTransactionById(event.transaction.id)
                                backStack.removeLastOrNull()
                            }

                            is TransactionInfoUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                        }
                    }
                )
            }

            entry<NavigationRoute.AnalyticsScreen> {
                val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()

                AnalyticsScreen(
                    uiState = analyticsViewModel.analyticsUiState.collectAsStateWithLifecycle().value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: AnalyticsUiEvent ->
                        when (event) {
                            is AnalyticsUiEvent.ShowTransactionInfo -> {
                                backStack.add(
                                    NavigationRoute.TransactionInfoScreen(event.transaction.id)
                                )
                            }

                            is AnalyticsUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }

                            is AnalyticsUiEvent.FilterAnalyticsDataByPeriod -> {
                                analyticsViewModel.filterAnalyticsByPeriod(event.period)
                            }

                            is AnalyticsUiEvent.FilterAnalyticsDataByPeriodOffset -> {
                                analyticsViewModel.filterAnalyticsByPeriodOffset(event.periodOffset)
                            }
                            is AnalyticsUiEvent.FilterAnalyticsDataByCategory -> {
                                analyticsViewModel.filterAnalyticsByCategory(event.transactionCategory)
                            }
                            is AnalyticsUiEvent.FilterAnalyticsDataByTransactionType -> {
                                analyticsViewModel.filterAnalyticsByTransactionType(event.transactionType)
                            }
                        }
                    }
                )
            }

        }
    )
}
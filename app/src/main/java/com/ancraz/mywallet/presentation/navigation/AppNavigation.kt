package com.ancraz.mywallet.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ancraz.mywallet.presentation.ui.events.AnalyticsUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.events.EditBalanceUiEvent
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionListUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletListUiEvent
import com.ancraz.mywallet.presentation.ui.screens.analytics.AnalyticsScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.home.HomeScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo.TransactionInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet.CreateWalletScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo.WalletInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo.EditWalletScreen

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
                HomeScreen(
                    paddingValues = innerPadding,
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
                            else -> Unit
                        }
                    },
                )
            }

            entry<NavigationRoute.EditBalanceScreen> { key ->
                val currentBalance = key.totalBalance

                EditBalanceScreen(
                    totalBalance = currentBalance,
                    paddingValues = innerPadding,
                    onEvent = { event: EditBalanceUiEvent ->
                        when (event) {
                            is EditBalanceUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                            else -> Unit
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionInputScreen> { key ->
                CreateTransactionScreen(
                    transactionType = key.transactionType,
                    paddingValues = innerPadding,
                    onEvent = { event: CreateTransactionUiEvent ->
                        when (event) {
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
                            else -> Unit
                        }
                    },
                )
            }

            entry<NavigationRoute.WalletListScreen> {
                WalletListScreen(
                    paddingValues = innerPadding,
                    onEvent = { event: WalletListUiEvent ->
                        when (event) {
                            is WalletListUiEvent.ShowWalletInfo -> {
                                backStack.add(
                                    NavigationRoute.WalletInfoScreen(event.wallet.id)
                                )
                            }

                            is WalletListUiEvent.CreateWallet -> {
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
                CreateWalletScreen(
                    paddingValues = innerPadding,
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<NavigationRoute.EditWalletScreen> {
                EditWalletScreen(
                    paddingValues = innerPadding,
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<NavigationRoute.WalletInfoScreen> { key ->
                WalletInfoScreen(
                    walletId = key.walletId,
                    paddingValues = innerPadding,
                    onEvent = { event: WalletInfoUiEvent ->
                        when (event) {
                            is WalletInfoUiEvent.EditWallet -> {
                                backStack.add(
                                    NavigationRoute.EditWalletScreen(key.walletId)
                                )
                            }
                            is WalletInfoUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                            else -> Unit
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionListScreen> {
                TransactionListScreen(
                    paddingValues = innerPadding,
                    onEvent = { event: TransactionListUiEvent ->
                        when (event) {
                            is TransactionListUiEvent.ShowTransactionInfo -> {
                                backStack.add(
                                    NavigationRoute.TransactionInfoScreen(event.transaction.id)
                                )
                            }

                            is TransactionListUiEvent.GoBack -> {
                                backStack.removeLastOrNull()
                            }
                            else -> Unit
                        }
                    }
                )
            }

            entry<NavigationRoute.TransactionInfoScreen> { key ->
                TransactionInfoScreen(
                    transactionId = key.transactionId,
                    paddingValues = innerPadding,
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<NavigationRoute.AnalyticsScreen> {
                AnalyticsScreen(
                    paddingValues =innerPadding,
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
                            else -> Unit
                        }
                    }
                )
            }
        }
    )
}
package com.ancraz.mywallet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.navigation.NavigationScreen
import com.ancraz.mywallet.presentation.ui.events.BuildWalletUiEvent
import com.ancraz.mywallet.presentation.ui.events.EditBalanceUiEvent
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionListUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletListUiEvent
import com.ancraz.mywallet.presentation.ui.screens.wallet.buildWallet.BuildWalletScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceUiState
import com.ancraz.mywallet.presentation.ui.screens.home.HomeScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo.WalletInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListScreen
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.viewModels.HomeViewModel
import com.ancraz.mywallet.presentation.viewModels.TransactionViewModel
import com.ancraz.mywallet.presentation.viewModels.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                MainActivityScreen()

            }
        }
    }
}


@Composable
private fun MainActivityScreen() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
    val transactionViewModel: TransactionViewModel = hiltViewModel<TransactionViewModel>()
    val walletViewModel = hiltViewModel<WalletViewModel>()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.HomeScreen.route
        ) {
            composable(
                route = NavigationScreen.HomeScreen.route
            ) {
                HomeScreen(
                    homeViewModel.homeUiState.value,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event ->
                        when (event) {
                            is HomeUiEvent.CreateTransaction -> {
                                when (event.transactionType) {
                                    TransactionType.INCOME -> {
                                        navigateToTransactionInputScreen(
                                            navController,
                                            homeViewModel.homeUiState.value.data.balance,
                                            event.transactionType
                                        )
                                    }

                                    TransactionType.EXPENSE -> {
                                        navigateToTransactionInputScreen(
                                            navController,
                                            homeViewModel.homeUiState.value.data.balance,
                                            event.transactionType
                                        )
                                    }

                                    TransactionType.TRANSFER -> {}
                                }
                            }

                            is HomeUiEvent.EditTotalBalance -> {
                                navigateToEditBalanceScreen(navController, event.currentBalance)
                            }

                            is HomeUiEvent.ShowWalletInfo -> {
                                walletViewModel.getWallet(event.wallet)
                                navController.navigate(NavigationScreen.WalletInfoScreen.route)
                            }

                            is HomeUiEvent.ShowTransactionInfo -> {
                                //todo implement
                            }

                            is HomeUiEvent.CreateWallet -> {
                                navController.navigate(
                                    NavigationScreen.BuildWalletScreen.route
                                )
                            }

                            is HomeUiEvent.SyncData -> {
                                homeViewModel.syncData()
                            }

                            is HomeUiEvent.ShowAllTransactions -> {
                                navController.navigate(NavigationScreen.TransactionListScreen.route)
                            }

                            is HomeUiEvent.ShowAllWallets -> {
                                navController.navigate(NavigationScreen.WalletListScreen.route)
                            }
                        }
                    },
                )
            }

            composable(
                route = NavigationScreen.EditBalanceScreen.route + "/{balance}"
            ) { navBackStackEntry ->

                val currentBalance = try {
                    (navBackStackEntry.arguments?.getString("balance") ?: "0").toFloat()
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    0f
                }

                EditBalanceScreen(
                    uiState = EditBalanceUiState(
                        data = EditBalanceUiState.EditBalanceScreenData(
                            currentTotalBalance = currentBalance
                        )
                    ),
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when (event) {
                            is EditBalanceUiEvent.UpdateBalanceValue -> {
                                homeViewModel.editTotalBalance(event.newBalance)
                            }

                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }

            composable(
                route = NavigationScreen.BuildWalletScreen.route
            ) { navBackStackEntry ->

                BuildWalletScreen(
                    uiState = walletViewModel.walletUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when (event) {
                            is BuildWalletUiEvent.AddWallet -> {
                                walletViewModel.addWallet(event.wallet)
                            }

                            is BuildWalletUiEvent.UpdateWallet -> {
                                walletViewModel.updateWallet(event.wallet)
                            }

                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }

            composable(
                route = NavigationScreen.TransactionInputScreen.route + "/{balance}" + "/{transaction}"
            ) { navBackStackEntry ->

                val currentBalanceValue = try {
                    (navBackStackEntry.arguments?.getString("balance") ?: "0").toFloat()
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    0f
                }

                val transactionType = try {
                    val transactionString =
                        (navBackStackEntry.arguments?.getString("transaction")) ?: ""
                    when (transactionString) {
                        TransactionType.EXPENSE.name -> TransactionType.EXPENSE
                        TransactionType.TRANSFER.name -> TransactionType.TRANSFER
                        else -> TransactionType.INCOME
                    }
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    TransactionType.INCOME
                }

                CreateTransactionScreen(
                    uiState = transactionViewModel.createTransactionUiState.value,
                    totalBalance = currentBalanceValue,
                    transactionType = transactionType,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when (event) {
                            is CreateTransactionUiEvent.AddTransaction -> {
                                transactionViewModel.addNewTransaction(event.transaction)
                            }

                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    },
                )
            }

            composable(route = NavigationScreen.TransactionListScreen.route) {
                TransactionListScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    uiState = transactionViewModel.transactionListUiState.value,
                    onEvent = { event: UiEvent ->
                        when(event) {
                            is TransactionListUiEvent.ShowTransactionInfo -> {
                                navController.navigate(NavigationScreen.TransactionInfoScreen.route)
                            }
                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }


            composable(route = NavigationScreen.WalletListScreen.route) {
                WalletListScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    uiState = walletViewModel.walletListUiState.value,
                    onEvent = { event: UiEvent ->
                        when(event){
                            is WalletListUiEvent.ShowWalletInfo -> {
                                walletViewModel.getWallet(event.wallet)
                                navController.navigate(NavigationScreen.WalletInfoScreen.route)
                            }
                            is WalletListUiEvent.CreateWallet -> {
                                navController.navigate(NavigationScreen.BuildWalletScreen.route)
                            }
                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }


            composable(route = NavigationScreen.WalletInfoScreen.route){
                WalletInfoScreen(
                    uiState = walletViewModel.walletUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when(event){
                            is WalletInfoUiEvent.EditWallet -> {
                                navController.navigate(NavigationScreen.BuildWalletScreen.route)
                            }

                            is WalletInfoUiEvent.DeleteWallet -> {
                                walletViewModel.deleteWallet(event.wallet)
                                navController.navigateUp()
                            }

                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }
        }
    }
}


private fun navigateToEditBalanceScreen(navController: NavController, balance: Float) {
    navController.navigate(NavigationScreen.EditBalanceScreen.route + "/$balance")
}

private fun navigateToTransactionInputScreen(
    navController: NavController,
    balance: Float,
    transactionType: TransactionType
) {
    navController.navigate(NavigationScreen.TransactionInputScreen.route + "/$balance" + "/${transactionType.name}")
}


@Preview
@Composable
private fun MainActivityScreenPreview() {
    MyWalletTheme {
        MainActivityScreen()
    }
}
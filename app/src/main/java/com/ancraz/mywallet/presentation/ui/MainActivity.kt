package com.ancraz.mywallet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.Constants
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.navigation.NavigationScreen
import com.ancraz.mywallet.presentation.ui.events.AnalyticsUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateWalletUiEvent
import com.ancraz.mywallet.presentation.ui.events.EditBalanceUiEvent
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.TransactionListUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.WalletListUiEvent
import com.ancraz.mywallet.presentation.ui.screens.analytics.AnalyticsScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet.CreateWalletScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalance.EditBalanceUiState
import com.ancraz.mywallet.presentation.ui.screens.home.HomeScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.CreateTransactionScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo.TransactionInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList.TransactionListScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo.WalletInfoScreen
import com.ancraz.mywallet.presentation.ui.screens.wallet.walletList.WalletListScreen
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.viewModels.AnalyticsViewModel
import com.ancraz.mywallet.presentation.viewModels.HomeViewModel
import com.ancraz.mywallet.presentation.viewModels.TransactionViewModel
import com.ancraz.mywallet.presentation.viewModels.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestinationValue = intent.extras?.getString(Constants.Widget.START_SCREEN_PATH_KEY)
        val startScreenRoute = getStartScreenRoute(startDestinationValue)

        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                MainActivityScreen(startScreenRoute)

            }
        }
    }


    private fun getStartScreenRoute(intentValue: String?): String{
        return intentValue?.let { key ->
            when (key){
                Constants.Widget.INCOME_SCREEN_PATH_VALUE -> NavigationScreen.TransactionInputScreen.route + "/${TransactionType.INCOME.name}"
                Constants.Widget.EXPENSE_SCREEN_PATH_VALUE -> NavigationScreen.TransactionInputScreen.route + "/${TransactionType.EXPENSE.name}"
                else -> NavigationScreen.HomeScreen.route
            }
        } ?: NavigationScreen.HomeScreen.route
    }
}


@Composable
private fun MainActivityScreen(startDestinationRoute: String) {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
    val transactionViewModel: TransactionViewModel = hiltViewModel<TransactionViewModel>()
    val walletViewModel: WalletViewModel = hiltViewModel<WalletViewModel>()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestinationRoute
        ) {
            composable(
                route = NavigationScreen.HomeScreen.route
            ) {
                HomeScreen(
                    uiState = homeViewModel.homeUiState.value,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event ->
                        when (event) {
                            is HomeUiEvent.CreateTransaction -> {
                                navController.navigate(
                                    NavigationScreen.TransactionInputScreen.route + "/${event.transactionType.name}"
                                )
                            }

                            is HomeUiEvent.EditTotalBalance -> {
                                navController.navigate(
                                    NavigationScreen.EditBalanceScreen.route + "/${event.currentBalance}"
                                )
                            }

                            is HomeUiEvent.ShowWalletInfo -> {
                                navController.navigate(
                                    NavigationScreen.WalletInfoScreen.route + "/${event.wallet.id}"
                                )
                            }

                            is HomeUiEvent.ShowTransactionInfo -> {
                                navController.navigate(
                                    NavigationScreen.TransactionInfoScreen.route + "/${event.transaction.id}"
                                )
                            }

                            is HomeUiEvent.CreateWallet -> {
                                navController.navigate(
                                    NavigationScreen.CreateWalletScreen.route
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

                            is HomeUiEvent.ShowAnalytics -> {
                                navController.navigate(NavigationScreen.AnalyticsScreen.route)
                            }

                            is HomeUiEvent.ChangePrivateMode -> {
                                homeViewModel.changePrivateMode(event.isPrivate)
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
                route = NavigationScreen.CreateWalletScreen.route
            ) { navBackStackEntry ->
                CreateWalletScreen(
                    uiState = walletViewModel.walletUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when (event) {
                            is CreateWalletUiEvent.AddWallet -> {
                                walletViewModel.addWallet(event.wallet)
                            }

                            is CreateWalletUiEvent.UpdateWallet -> {
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
                route = NavigationScreen.TransactionInputScreen.route  + "/{transaction}"
            ) { navBackStackEntry ->

                val transactionType = try {
                    val transactionString =
                        (navBackStackEntry.arguments?.getString("transaction")) ?: ""
                    when (transactionString) {
                        TransactionType.EXPENSE.name -> TransactionType.EXPENSE
                        TransactionType.TRANSFER.name -> TransactionType.TRANSFER
                        else -> TransactionType.INCOME
                    }
                } catch (e: Exception) {
                    TransactionType.INCOME
                }

                CreateTransactionScreen(
                    uiState = transactionViewModel.createTransactionUiState.value,
                    transactionType = transactionType,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when (event) {
                            is CreateTransactionUiEvent.AddTransaction -> {
                                transactionViewModel.addNewTransaction(event.transaction)
                                navController.navigate(NavigationScreen.HomeScreen.route){
                                    popUpTo(NavigationScreen.TransactionInputScreen.route + "/{transaction}"){
                                        inclusive = true
                                    }
                                }
                            }

                            is CreateTransactionUiEvent.CreateWallet -> {
                                navController.navigate(NavigationScreen.CreateWalletScreen.route)
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
                                navController.navigate(NavigationScreen.TransactionInfoScreen.route + "/${event.transaction.id}")
                            }
                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }


            composable(route = NavigationScreen.TransactionInfoScreen.route + "/{transactionId}") {  navBackStackEntry ->
                val transactionId = try {
                    navBackStackEntry.arguments?.getString("transactionId")?.toLong()
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    null
                }
                transactionId?.let {
                    transactionViewModel.getTransactionById(it)
                } ?: run {
                    debugLog("walletId is null")
                }
                TransactionInfoScreen(
                    uiState = transactionViewModel.transactionInfoUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when(event){
                            is TransactionInfoUiEvent.DeleteTransaction -> {
                                transactionViewModel.deleteTransactionById(event.transaction.id)
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


            composable(route = NavigationScreen.WalletListScreen.route) {
                WalletListScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    uiState = walletViewModel.walletListUiState.value,
                    onEvent = { event: UiEvent ->
                        when(event){
                            is WalletListUiEvent.ShowWalletInfo -> {
                                navController.navigate(NavigationScreen.WalletInfoScreen.route + "/${event.wallet.id}")
                            }
                            is WalletListUiEvent.CreateWallet -> {
                                navController.navigate(NavigationScreen.CreateWalletScreen.route)
                            }
                            is UiEvent.GoBack -> {
                                navController.navigateUp()
                            }

                            else -> {}
                        }
                    }
                )
            }


            composable(route = NavigationScreen.WalletInfoScreen.route + "/{walletId}"){ navBackStackEntry ->
                LaunchedEffect(key1 = true) {
                    val walletId = try {
                        navBackStackEntry.arguments?.getString("walletId")?.toLong()
                    } catch (e: Exception) {
                        debugLog("getCurrentBalance argument exception: ${e.message}")
                        null
                    }
                    walletId?.let {
                        walletViewModel.getWalletById(it)
                    } ?: run {
                        debugLog("walletId is null")
                    }
                }

                WalletInfoScreen(
                    uiState = walletViewModel.walletUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when(event){
                            is WalletInfoUiEvent.EditWallet -> {
                                navController.navigate(NavigationScreen.CreateWalletScreen.route)
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

            composable(route = NavigationScreen.AnalyticsScreen.route){
                val analyticsViewModel = hiltViewModel<AnalyticsViewModel>()

                AnalyticsScreen(
                    uiState = analyticsViewModel.analyticsUiState.value,
                    modifier = Modifier
                        .padding(innerPadding),
                    onEvent = { event: UiEvent ->
                        when(event){
                            is AnalyticsUiEvent.ShowTransactionInfo -> {
                                navController.navigate(NavigationScreen.TransactionInfoScreen.route + "/${event.transaction.id}")
                            }
                            is AnalyticsUiEvent.FilterAnalyticsData -> {
                                analyticsViewModel.filterAnalyticsData(
                                    transactionType = event.type,
                                    transactionCategory = event.category,
                                    period = event.period,
                                    periodOffset = event.periodOffset
                                )
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


@Preview
@Composable
private fun MainActivityScreenPreview() {
    MyWalletTheme {
        MainActivityScreen(NavigationScreen.HomeScreen.route)
    }
}
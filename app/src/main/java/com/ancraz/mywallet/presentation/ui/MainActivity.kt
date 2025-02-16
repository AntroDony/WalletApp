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
import com.ancraz.mywallet.presentation.ui.screens.createWalletScreen.CreateWalletScreen
import com.ancraz.mywallet.presentation.ui.screens.editBalanceScreen.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.homeScreen.HomeScreen
import com.ancraz.mywallet.presentation.ui.screens.inputScreen.TransactionInputScreen
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
                    homeViewModel.homeScreenState.value,
                    modifier = Modifier.padding(innerPadding),
                    onMadeTransaction = { transactionType ->
                        when(transactionType){
                            TransactionType.INCOME -> {
                                navigateToTransactionInputScreen(
                                    navController,
                                    homeViewModel.homeScreenState.value.data.balance,
                                    transactionType
                                )
                            }
                            TransactionType.EXPENSE -> {
                                navigateToTransactionInputScreen(
                                    navController,
                                    homeViewModel.homeScreenState.value.data.balance,
                                    transactionType
                                )
                            }
                            TransactionType.TRANSFER -> {}
                        }
                    },
                    onEditBalance = { currentBalance ->
                        navigateToEditBalanceScreen(navController, currentBalance)
                    },
                    onCreateWallet = {
                        navController.navigate(
                            NavigationScreen.CreateWalletScreen.route
                        )
                    },
                    onEditWallet = {

                    }
                )
            }

            composable(
                route = NavigationScreen.EditBalanceScreen.route + "/{balance}"
            ) { navBackStackEntry ->

                val currentBalanceValue = try {
                    (navBackStackEntry.arguments?.getString("balance") ?: "0").toFloat()
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    0f
                }


                EditBalanceScreen(
                    value = currentBalanceValue,
                    modifier = Modifier.padding(innerPadding),
                    onUpdateBalanceValue = { value ->
                        homeViewModel.editTotalBalance(value)
                    },
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(
                route = NavigationScreen.CreateWalletScreen.route
            ){ navBackStackEntry ->
                val walletViewModel = hiltViewModel<WalletViewModel>()

                CreateWalletScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    onAddWallet = {
                        //todo implement
                    },
                    onBack = {
                        navController.navigateUp()
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
                    val transactionString = (navBackStackEntry.arguments?.getString("transaction")) ?: ""
                    when(transactionString){
                        TransactionType.EXPENSE.name -> TransactionType.EXPENSE
                        TransactionType.TRANSFER.name -> TransactionType.TRANSFER
                        else -> TransactionType.INCOME
                    }
                } catch (e: Exception) {
                    debugLog("getCurrentBalance argument exception: ${e.message}")
                    TransactionType.INCOME
                }

                TransactionInputScreen(
                    uiState = transactionViewModel.transactionUiState.value,
                    totalBalance = currentBalanceValue,
                    transactionType = transactionType,
                    modifier = Modifier.padding(innerPadding),
                    onAddTransaction = { transaction ->
                        transactionViewModel.addNewTransaction(transaction)
                    },
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}


private fun navigateToEditBalanceScreen(navController: NavController, balance: Float) {
    navController.navigate(NavigationScreen.EditBalanceScreen.route + "/$balance")
}

private fun navigateToTransactionInputScreen(navController: NavController, balance: Float, transactionType: TransactionType){
    navController.navigate(NavigationScreen.TransactionInputScreen.route + "/$balance" + "/${transactionType.name}")
}


@Preview
@Composable
private fun MainActivityScreenPreview() {
    MyWalletTheme {
        MainActivityScreen()
    }
}
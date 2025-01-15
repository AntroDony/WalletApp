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
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.navigation.NavigationRoute
import com.ancraz.mywallet.presentation.ui.screens.editBalanceScreen.EditBalanceScreen
import com.ancraz.mywallet.presentation.ui.screens.homeScreen.HomeScreen
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.viewModels.HomeViewModel
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.HomeScreen.route
        ) {
            composable(
                route = NavigationRoute.HomeScreen.route
            ) {
                HomeScreen(
                    homeViewModel.totalBalanceState.value,
                    modifier = Modifier.padding(innerPadding),
                    onTransaction = {

                    },
                    onEditBalance = { currentBalance ->
                        navigateToEditBalanceScreen(navController, currentBalance)
                    }
                )
            }

            composable(
                route = NavigationRoute.EditBalanceScreen.route + "/{balance}"
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
                        homeViewModel.updateTotalBalance(value)
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}


private fun navigateToEditBalanceScreen(navController: NavController, balance: Float){
    navController.navigate(NavigationRoute.EditBalanceScreen.route + "/$balance")
}


@Preview
@Composable
private fun MainActivityScreenPreview() {
    MyWalletTheme {
        MainActivityScreen()
    }
}
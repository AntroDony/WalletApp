package com.ancraz.mywallet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.Constants
import com.ancraz.mywallet.presentation.navigation.BasicNavigation
import com.ancraz.mywallet.presentation.navigation.NavigationRoute
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
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


    private fun getStartScreenRoute(intentValue: String?): NavigationRoute{
        return intentValue?.let { key ->
            when (key){
                Constants.Widget.INCOME_SCREEN_PATH_VALUE -> NavigationRoute.TransactionInputScreen(TransactionType.INCOME)
                Constants.Widget.EXPENSE_SCREEN_PATH_VALUE -> NavigationRoute.TransactionInputScreen(TransactionType.EXPENSE)
                else -> NavigationRoute.HomeScreen
            }
        } ?: NavigationRoute.HomeScreen
    }
}


@Composable
private fun MainActivityScreen(startDestinationRoute: NavigationRoute) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        BasicNavigation(
            startDestination = startDestinationRoute,
            innerPadding = innerPadding
        )
    }
}


@Preview
@Composable
private fun MainActivityScreenPreview() {
    MyWalletTheme {
        MainActivityScreen(NavigationRoute.HomeScreen)
    }
}
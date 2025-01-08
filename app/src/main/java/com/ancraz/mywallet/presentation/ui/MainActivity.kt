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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.viewModels.MainViewModel
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
private fun MainActivityScreen(){

    val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        innerPadding

        mainViewModel.currenciesRateState.value
    }
}


@Preview
@Composable
private fun MainActivityScreenPreview(){
    MyWalletTheme {
        MainActivityScreen()
    }
}
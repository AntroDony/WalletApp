package com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ancraz.mywallet.presentation.ui.screens.wallet.components.WalletConfigContainer

@Composable
fun EditWalletScreen(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    viewModel: WalletInfoViewModel = hiltViewModel<WalletInfoViewModel>(),
){
    WalletConfigContainer(
        uiState = viewModel.walletUiState.collectAsStateWithLifecycle().value,
        modifier = Modifier.padding(paddingValues),
        onActionButtonClicked = { wallet ->
            viewModel.updateWallet(wallet)
            onBack()
        },
        onBack = {
            onBack()
        }
    )
}
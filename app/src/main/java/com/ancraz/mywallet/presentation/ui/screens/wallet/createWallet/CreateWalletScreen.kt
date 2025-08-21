package com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ancraz.mywallet.presentation.ui.screens.wallet.components.WalletConfigContainer

@Composable
fun CreateWalletScreen(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    viewModel: CreateWalletViewModel = hiltViewModel<CreateWalletViewModel>(),
){

    WalletConfigContainer(
        uiState = viewModel.walletUiState.collectAsStateWithLifecycle().value,
        modifier = Modifier.padding(paddingValues),
        onActionButtonClicked = { wallet ->
            viewModel.addWallet(wallet)
            onBack()
        },
        onBack = {
            onBack()
        }
    )
}
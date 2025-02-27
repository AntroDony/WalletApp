package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor

@Composable
fun TransactionInfoScreen(
    uiState: TransactionInfoUiState,
    modifier: Modifier = Modifier,
    onEvent: () -> Unit
){


}


@Preview
@Composable
private fun TransactionInfoScreenPreview(){
    MyWalletTheme {
        TransactionInfoScreen(
            modifier = Modifier.background(backgroundColor),
            uiState = TransactionInfoUiState(

            ),
            onEvent = {}
        )
    }
}
package com.ancraz.mywallet.presentation.ui.screens.inputScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme

@Composable
fun InputTransactionValueScreen(
    value: Float = 0f,
    transactionType: TransactionType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize()
    ) {  }
}


@Preview
@Composable
fun InputTransactionValueScreenPreview(){
    MyWalletTheme {
        InputTransactionValueScreen(
            8000f,
            TransactionType.INCOME
        )
    }
}
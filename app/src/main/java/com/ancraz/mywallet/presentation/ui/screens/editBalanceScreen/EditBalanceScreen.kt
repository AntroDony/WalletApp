package com.ancraz.mywallet.presentation.ui.screens.editBalanceScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.TransactionConfigContainer
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.SubmitButton
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun EditBalanceScreen(
    value: Float,
    currencyCode: CurrencyCode = CurrencyCode.USD,
    modifier: Modifier = Modifier,
    onUpdateBalanceValue: (Float) -> Unit,
    onNavigateBack: () -> Unit
) {
    val valueState = remember { mutableStateOf(value.toFormattedString()) }
    val currencyState = remember { mutableStateOf(currencyCode) }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.Center
    ) {
        NavigationToolbar(title = "Edit Balance", onClickBack = onNavigateBack)

        HorizontalSpacer()

        TransactionConfigContainer(
            valueState = valueState,
            currencyState = currencyState,
            title = "Total Balance",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )

        HorizontalSpacer()

        InputNumberKeyboard(
            valueState
        )

        HorizontalSpacer()

        SubmitButton(
            title = "Update",
            onClick = {
                val newBalanceValue = valueState.value.toFloatValue()
                onUpdateBalanceValue(newBalanceValue)
                onNavigateBack()
            }
        )
    }
}



@Preview
@Composable
fun EditBalancePreview() {
    MyWalletTheme {
        EditBalanceScreen(
            value = 8000f,
            onUpdateBalanceValue = {},
            onNavigateBack = {})
    }
}
package com.ancraz.mywallet.presentation.ui.screens.editBalanceScreen

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
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.InputTextField
import com.ancraz.mywallet.presentation.ui.components.KeyboardAction
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.SubmitButton
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
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
    Column(
        modifier = modifier.fillMaxSize()
            .padding(14.dp)
    ) {
        NavigationToolbar(title = "Edit Balance", onClickBack = onNavigateBack)

        HorizontalSpacer()

        InputTextField(
            valueState = valueState,
            title = "Total Balance",
            currencyCode = currencyCode,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )

        HorizontalSpacer()

        InputNumberKeyboard { action ->
            when (action) {
                is KeyboardAction.Number -> {
                    if (valueState.value.isEmpty() || valueState.value.toFloat() == 0f) {
                        valueState.value = action.number.toString()
                    } else {
                        valueState.value += action.number
                    }
                }

                is KeyboardAction.Decimal -> {
                    if (!valueState.value.contains(".") && valueState.value.isNotEmpty()) {
                        valueState.value += "."
                    }
                }

                is KeyboardAction.Delete -> {
                    debugLog("try delete value :${valueState.value}")
                    valueState.value = valueState.value.dropLast(1)
                }
            }
        }

        HorizontalSpacer()

        SubmitButton(
            title = "Update",
            onClick = {
                val newBalanceValue = convertStringToFloat(valueState.value)
                onUpdateBalanceValue(newBalanceValue)
                onNavigateBack()
            }
        )
    }
}


private fun convertStringToFloat(balanceStr: String): Float {
    return try {
        balanceStr.toFloat()
    } catch (e: Exception) {
        debugLog("convertStringToFloat exception: ${e.message}")
        0f
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
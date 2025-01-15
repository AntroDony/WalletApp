package com.ancraz.mywallet.presentation.ui.screens.editBalanceScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.InputTextField
import com.ancraz.mywallet.presentation.ui.components.KeyboardAction
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme

@Composable
fun EditBalanceScreen(
    value: Float,
    modifier: Modifier = Modifier,
    onUpdateBalanceValue: (Float) -> Unit
) {
    val valueState = remember { mutableStateOf(value.toString()) }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InputTextField(
            valueState = valueState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                val newBalanceValue = convertStringToFloat(valueState.value)
                onUpdateBalanceValue(newBalanceValue)
            }
        ) {
            Text(
                text = "Update Balance"
            )
        }
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
        EditBalanceScreen(value = 8000f) {}
    }
}
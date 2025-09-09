package com.ancraz.mywallet.presentation.ui.screens.wallet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import kotlinx.coroutines.delay
import kotlin.collections.forEach

@Composable
fun EditAccountValueDialog(
    value: String,
    modifier: Modifier = Modifier,
    onResult: (String) -> Unit,
    onCancel: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = if (value.toFloat() == 0f) "" else value
            )
        )
    }

    LaunchedEffect(key1 = true) {
        delay(100)
        focusRequester.requestFocus()
        textFieldValue.value = textFieldValue.value.copy(
            selection = TextRange(textFieldValue.value.text.length)
        )
    }

    Dialog(
        onDismissRequest = {
            onCancel()
        },
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(width = 1.dp, color = primaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.edit_wallet_account_value_title),
                    color = onBackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(height = 20.dp)

                OutlinedTextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                HorizontalSpacer(height = 20.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onCancel() },
                    ) {
                        Text(
                            text = stringResource(R.string.edit_wallet_cancel_button),
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    TextButton(
                        onClick = {
                            onResult(textFieldValue.value.text)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.edit_wallet_save_button),
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectNewAccountCurrencyDialog(
    currencies: List<CurrencyCode>,
    modifier: Modifier = Modifier,
    onResult: (CurrencyCode) -> Unit,
    onCancel: () -> Unit
) {
    val selectedCurrency = remember { mutableStateOf(CurrencyCode.USD) }

    Dialog(
        onDismissRequest = {
            onCancel()
        }
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(width = 1.dp, color = primaryColor)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.select_wallet_account_currency_title),
                    color = onBackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(height = 20.dp)

                currencies.forEach { currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = currency == selectedCurrency.value,
                            onClick = {
                                selectedCurrency.value = currency
                            }
                        )

                        VerticalSpacer()

                        Text(
                            text = currency.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = onBackgroundColor
                        )
                    }
                }

                HorizontalSpacer(height = 20.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onCancel() },
                    ) {
                        Text(
                            text = stringResource(R.string.edit_wallet_cancel_button),
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    TextButton(
                        onClick = {
                            onResult(selectedCurrency.value)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.edit_wallet_save_button),
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
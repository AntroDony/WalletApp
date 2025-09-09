package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun TransactionConfigContainer(
    valueState: MutableState<String>,
    currencyState: MutableState<CurrencyCode>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = onSurfaceColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )

        HorizontalSpacer()

        Text(
            text = valueState.value.ifEmpty { "0.00" },
            color = onSurfaceColor,
            fontSize = 60.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        CurrencyDropDownMenu(
            currentCurrencyState = currencyState
        )
    }

}


@Preview
@Composable
fun InputTextFieldPreview() {
    val valueState = remember { mutableStateOf(8000f.toFormattedString()) }
    val currencyState = remember { mutableStateOf(CurrencyCode.EUR) }
    MyWalletTheme {
        TransactionConfigContainer(
            valueState = valueState,
            currencyState = currencyState,
            title = "Balance",
            modifier = Modifier.background(backgroundColor),
        )
    }
}

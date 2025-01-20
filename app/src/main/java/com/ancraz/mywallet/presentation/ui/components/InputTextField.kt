package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun InputTextField(
    valueState: MutableState<String>,
    currencyState: MutableState<CurrencyCode>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = onSurfaceColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
        )

        HorizontalSpacer(Modifier.height(20.dp))

        Text(
            text = valueState.value,
            color = onSurfaceColor,
            fontSize = 60.sp,
            modifier = Modifier
        )

        HorizontalSpacer()

        CurrencyDropDownMenu(
            currentCurrencyState = currencyState
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

    }

}


@Preview
@Composable
fun InputTextFieldPreview(){
    val valueState = remember { mutableStateOf(8000f.toFormattedString()) }
    val currencyState = remember { mutableStateOf(CurrencyCode.EUR) }
    MyWalletTheme {
        InputTextField(
            valueState = valueState,
            currencyState = currencyState,
            title = "Balance"
        )
    }
}

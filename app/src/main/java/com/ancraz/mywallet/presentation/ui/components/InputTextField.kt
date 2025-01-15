package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme

@Composable
fun InputTextField(
    valueState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(12.dp)
    ) {
        Text(
            text = valueState.value.toString(),
            color = Color.White,
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }

}


@Preview
@Composable
fun InputTextFieldPreview(){
    val valueState = remember { mutableStateOf(8000f.toString()) }
    MyWalletTheme {
        InputTextField(
            valueState
        )
    }
}

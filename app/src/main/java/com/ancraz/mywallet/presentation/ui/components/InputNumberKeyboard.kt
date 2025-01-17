package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.outlineColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun InputNumberKeyboard(
    inputValueState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(Keys){ key ->
            KeyboardButton(key, onClick = { action ->
                when (action) {
                    is KeyboardAction.Number -> {
                        if (inputValueState.value.isEmpty() || inputValueState.value.toFloat() == 0f) {
                            inputValueState.value = action.number.toString()
                        } else {
                            inputValueState.value += action.number
                        }
                    }

                    is KeyboardAction.Decimal -> {
                        if (!inputValueState.value.contains(".") && inputValueState.value.isNotEmpty()) {
                            inputValueState.value += "."
                        }
                    }

                    is KeyboardAction.Delete -> {
                        inputValueState.value = inputValueState.value.dropLast(1)
                    }
                }
            })
        }
    }
}

@Composable
private fun KeyboardButton(
    keyboardKey: KeyboardKey,
    modifier: Modifier = Modifier,
    onClick: (KeyboardAction) -> Unit
){
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(outlineColor)
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onClick(keyboardKey.onAction)
            }
    ) {
        if (keyboardKey.icon != null){
            Image(
                imageVector = keyboardKey.icon,
                contentDescription = keyboardKey.symbol,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(26.dp)
            )
        } else {
            Text(
                text = keyboardKey.symbol,
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.Center)

            )

        }
    }
}


private val Keys = listOf(
    KeyboardKey(symbol = "1", onAction = KeyboardAction.Number(1)),
    KeyboardKey(symbol = "2", onAction = KeyboardAction.Number(2)),
    KeyboardKey(symbol = "3", onAction = KeyboardAction.Number(3)),
    KeyboardKey(symbol = "4", onAction = KeyboardAction.Number(4)),
    KeyboardKey(symbol = "5", onAction = KeyboardAction.Number(5)),
    KeyboardKey(symbol = "6", onAction = KeyboardAction.Number(6)),
    KeyboardKey(symbol = "7", onAction = KeyboardAction.Number(7)),
    KeyboardKey(symbol = "8", onAction = KeyboardAction.Number(8)),
    KeyboardKey(symbol = "9", onAction = KeyboardAction.Number(9)),
    KeyboardKey(symbol = ".", onAction = KeyboardAction.Decimal),
    KeyboardKey(symbol = "0", onAction = KeyboardAction.Number(0)),
    KeyboardKey(symbol = "DEL", icon = Icons.AutoMirrored.Outlined.Backspace, onAction = KeyboardAction.Delete)
)


@Preview
@Composable
private fun InputNumberKeyboardPreview(){
    val inputValue = remember { mutableStateOf(8000f.toFormattedString()) }
    MyWalletTheme {
        InputNumberKeyboard(inputValue)
    }
}
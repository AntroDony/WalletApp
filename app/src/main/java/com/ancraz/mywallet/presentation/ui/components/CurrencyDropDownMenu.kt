package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.surfaceColor

@Composable
fun CurrencyDropDownMenu(
    currentCurrencyState: MutableState<CurrencyCode>,
    modifier: Modifier = Modifier
) {
    val items = CurrencyCode.entries.toList()

    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableStateOf(items.indexOf(currentCurrencyState.value)) }

    Box(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }
        ) {
            Text(
                text = items[itemPosition.value].name,
                color = onSurfaceColor,
                fontSize = 26.sp,
                modifier = Modifier.padding(8.dp)
            )

            Image(
                imageVector = if (isDropDownExpanded.value) {
                    Icons.Filled.ArrowDropUp
                } else  {
                    Icons.Filled.ArrowDropDown
                },
                contentDescription = "Select currency",
                colorFilter = ColorFilter.tint(onSurfaceColor),
                modifier = Modifier
                    .size(38.dp)
            )
        }

        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            },
            modifier = Modifier
                .background(surfaceColor)

        ) {
            items.forEachIndexed { index, currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = currency.name,
                            color = onSurfaceColor,
                            fontSize = 20.sp
                        )
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                    }
                )

            }
        }
    }
}


@Preview
@Composable
private fun CurrencyDropDownMenuPreview() {
    val currencyState = remember { mutableStateOf(CurrencyCode.USD) }
    MyWalletTheme {
        CurrencyDropDownMenu(currencyState)
    }
}
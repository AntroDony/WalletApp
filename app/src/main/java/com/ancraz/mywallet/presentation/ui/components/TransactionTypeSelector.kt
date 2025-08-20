package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor

@Composable
fun TransactionTypeSelector(
    selectedType: TransactionType?,
    modifier: Modifier = Modifier,
    onTypeSelected: (TransactionType?) -> Unit
){
    val selectedTypeState = remember { mutableStateOf(selectedType) }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(secondaryColor)
    ) {
        TransactionTypeItem(
            typeName = "All",
            isSelected = selectedTypeState.value == null,
            modifier = Modifier.weight(1f),
            onClick = {
                selectedTypeState.value = null
                onTypeSelected(null)
            }
        )

        TransactionType.entries.dropLast(1).forEach { type ->
            TransactionTypeItem(
                typeName = type.name,
                isSelected = type == selectedTypeState.value,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedTypeState.value = type
                    onTypeSelected(type)
                }
            )
        }
    }
}


@Composable
private fun TransactionTypeItem(
    typeName: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) primaryColor else secondaryColor
            )
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = typeName.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) onPrimaryColor else onSecondaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}


@Preview
@Composable
private fun TransactionTypeSelectorPreview(){

    MyWalletTheme {
        TransactionTypeSelector(
            selectedType = null,
            onTypeSelected = {}
        )
    }
}
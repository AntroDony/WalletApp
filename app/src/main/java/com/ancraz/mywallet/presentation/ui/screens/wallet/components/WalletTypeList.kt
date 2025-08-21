package com.ancraz.mywallet.presentation.ui.screens.wallet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor

@Composable
fun WalletTypeList(
    walletTypes: List<WalletType>,
    selectedType: MutableState<WalletType?>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
        verticalItemSpacing = 6.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(walletTypes) { walletType ->
            WalletTypeItem(
                type = walletType,
                selectedType = selectedType
            )
        }
    }
}


@Composable
private fun WalletTypeItem(
    type: WalletType,
    selectedType: MutableState<WalletType?>,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedType.value == type

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) primaryColor else backgroundColor
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(width = 1.dp, color = primaryColor),
        modifier = modifier,
        onClick = {
            selectedType.value = type
        }
    ) {
        Text(
            text = type.walletName,
            fontSize = 14.sp,
            maxLines = 1,
            color = if (isSelected) backgroundColor else onSurfaceColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
    }
}
package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor

@Composable
fun InfoRow(
    title: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = title,
            color = primaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        VerticalSpacer()

        Text(
            text = info,
            color = onBackgroundColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
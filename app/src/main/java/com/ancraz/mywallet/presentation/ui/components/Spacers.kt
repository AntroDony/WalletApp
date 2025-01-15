package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(
    modifier: Modifier = Modifier.height(14.dp)
){
    Spacer(modifier = modifier)
}

@Composable
fun VerticalSpacer(
    modifier: Modifier = Modifier.width(14.dp)
){
    Spacer(modifier = modifier)
}
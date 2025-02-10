package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(
    height: Dp = 14.dp,
    modifier: Modifier = Modifier
){
    Spacer(modifier = modifier.height(height))
}

@Composable
fun VerticalSpacerr(
    width: Dp,
    modifier: Modifier = Modifier
){
    Spacer(modifier = modifier.width(width))
}
package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.outlineColor

@Composable
fun NavigationToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = onSurfaceColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Center)
        )

        BackButton(onClick = onClickBack)
    }
}


@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            //.padding(vertical = 14.dp)
            .clip(CircleShape)
            .background(outlineColor)
            .clickable {
                onClick()
            }

    ){
        Image(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
            contentDescription = "Back",
            colorFilter = ColorFilter.tint(onSurfaceColor),
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        )
    }
}

@Preview
@Composable
private fun NavigationToolbarPreview(){
    MyWalletTheme {
        NavigationToolbar(title = "Toolbar") { }
    }
}
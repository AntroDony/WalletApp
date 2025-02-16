package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor

@Composable
fun SubmitButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryColor
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = title.uppercase(),
            color = onPrimaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                //.padding(vertical = 4.dp)
        )
    }
}
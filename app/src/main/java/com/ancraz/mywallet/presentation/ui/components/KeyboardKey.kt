package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

data class KeyboardKey(
    val symbol: String,
    val icon: ImageVector? = null,
    val onAction: KeyboardAction
)


sealed class KeyboardAction{
    data class Number(val number: Int): KeyboardAction()
    object Delete: KeyboardAction()
    object Decimal: KeyboardAction()
}

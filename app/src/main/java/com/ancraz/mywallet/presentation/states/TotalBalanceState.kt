package com.ancraz.mywallet.presentation.states

data class TotalBalanceState(
    val isLoading: Boolean = false,
    val balance: Float = 0f,
    val error: String? = null
)
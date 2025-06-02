package com.ancraz.mywallet.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.ancraz.mywallet.core.models.TransactionType
import kotlinx.serialization.Serializable

sealed class NavigationRoute: NavKey {

    @Serializable
    object HomeScreen: NavigationRoute()

    @Serializable
    data class EditBalanceScreen(val totalBalance: Float): NavigationRoute()

    @Serializable
    data class TransactionInputScreen(val transactionType: TransactionType): NavigationRoute()

    @Serializable
    object WalletListScreen: NavigationRoute()

    @Serializable
    object TransactionListScreen: NavigationRoute()

    @Serializable
    object AnalyticsScreen: NavigationRoute()

    @Serializable
    object CreateWalletScreen: NavigationRoute()

    @Serializable
    data class WalletInfoScreen(val walletId: Long): NavigationRoute()

    @Serializable
    data class TransactionInfoScreen(val transactionId: Long): NavigationRoute()


}
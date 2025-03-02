package com.ancraz.mywallet.presentation.navigation

sealed class NavigationScreen(val route: String) {

    object HomeScreen: NavigationScreen("home")
    object EditBalanceScreen: NavigationScreen("edit_balance")
    object TransactionInputScreen: NavigationScreen("transaction_input")

    object WalletListScreen: NavigationScreen("wallet_list")
    object CreateWalletScreen: NavigationScreen("build_wallet")
    object WalletInfoScreen: NavigationScreen("wallet_info")

    object TransactionListScreen: NavigationScreen("transaction_list")
    object TransactionInfoScreen: NavigationScreen("transaction_info")

}
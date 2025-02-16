package com.ancraz.mywallet.presentation.navigation

sealed class NavigationScreen(val route: String) {

    object HomeScreen: NavigationScreen("home")
    object EditBalanceScreen: NavigationScreen("edit_balance")
    object TransactionInputScreen: NavigationScreen("transaction_input")
    object CreateWalletScreen: NavigationScreen("create_wallet")
    object WalletInfoScreen: NavigationScreen("wallet_info")
    object SelectTransactionCategoryScreen: NavigationScreen("select_transaction_category")
    object TransactionListScreen: NavigationScreen("transaction_list")
    object AccountInfoScreen: NavigationScreen("account_info")
}
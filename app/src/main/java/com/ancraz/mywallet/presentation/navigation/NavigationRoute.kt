package com.ancraz.mywallet.presentation.navigation

sealed class NavigationRoute(val route: String) {

    object HomeScreen: NavigationRoute("home_screen")
    object EditBalanceScreen: NavigationRoute("edit_balance_screen")
    object InputTransactionValueScreen: NavigationRoute("input_transaction_value_screen")
    object SelectTransactionCategoryScreen: NavigationRoute("select_transaction_category_screen")
    object TransactionListScreen: NavigationRoute("transactions_screen")
    object AccountInfoScreen: NavigationRoute("account_info_screen")
}
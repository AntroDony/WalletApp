package com.ancraz.mywallet.presentation.ui.events

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi

sealed class HomeUiEvent{
    data class CreateTransaction(val transactionType: TransactionType): HomeUiEvent()
    data class EditTotalBalance(val currentBalance: Float): HomeUiEvent()

    data class ShowWalletInfo(val wallet: WalletUi): HomeUiEvent()
    data class ShowTransactionInfo(val transaction: TransactionUi): HomeUiEvent()

    data class ChangePrivateMode(val isPrivate: Boolean): HomeUiEvent()

    data object CreateWallet: HomeUiEvent()
    data object SyncData: HomeUiEvent()

    data object ShowAllWallets: HomeUiEvent()
    data object ShowAllTransactions: HomeUiEvent()
    data object ShowAnalytics: HomeUiEvent()
}

sealed class EditBalanceUiEvent{
    data class UpdateBalanceValue(val newBalance: Float): EditBalanceUiEvent()

    data object GoBack: EditBalanceUiEvent()
}

sealed class CreateTransactionUiEvent{
    data class AddTransaction(val transaction: TransactionUi): CreateTransactionUiEvent()
    data object CreateWallet: CreateTransactionUiEvent()

    data object GoBack: CreateTransactionUiEvent()
}

sealed class TransactionListUiEvent {
    data class ShowTransactionInfo(val transaction: TransactionUi): TransactionListUiEvent()
    data object GoBack: TransactionListUiEvent()
}
sealed class WalletListUiEvent{
    data object CreateWallet: WalletListUiEvent()
    data class ShowWalletInfo(val wallet: WalletUi): WalletListUiEvent()

    data object GoBack: WalletListUiEvent()
}

sealed class WalletInfoUiEvent {
    data class EditWallet(val wallet: WalletUi): WalletInfoUiEvent()
    data class DeleteWallet(val wallet: WalletUi): WalletInfoUiEvent()

    data object GoBack: WalletInfoUiEvent()
}

sealed class AnalyticsUiEvent{

    data class FilterAnalyticsDataByTransactionType(
        val transactionType: TransactionType?
    ): AnalyticsUiEvent()

    data class FilterAnalyticsDataByCategory(
        val transactionCategory: TransactionCategoryUi?
    ): AnalyticsUiEvent()

    data class FilterAnalyticsDataByPeriod(
        val period: AnalyticsPeriod
    ): AnalyticsUiEvent()

    data class FilterAnalyticsDataByPeriodOffset(
        val periodOffset: Int
    ): AnalyticsUiEvent()

    data class ShowTransactionInfo(val transaction: TransactionUi): AnalyticsUiEvent()

    data object GoBack: AnalyticsUiEvent()
}


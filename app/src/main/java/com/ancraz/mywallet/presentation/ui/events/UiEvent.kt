package com.ancraz.mywallet.presentation.ui.events

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi

sealed interface UiEvent {
    data object GoBack: UiEvent
}

sealed class HomeUiEvent: UiEvent{
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

sealed class EditBalanceUiEvent: UiEvent{
    data class UpdateBalanceValue(val newBalance: Float): EditBalanceUiEvent()
}

sealed class CreateWalletUiEvent: UiEvent{
    data class AddWallet(val wallet: WalletUi): CreateWalletUiEvent()
    data class UpdateWallet(val wallet: WalletUi): CreateWalletUiEvent()
}

sealed class CreateTransactionUiEvent: UiEvent{
    data class AddTransaction(val transaction: TransactionUi): CreateTransactionUiEvent()
    data object CreateWallet: CreateTransactionUiEvent()
}

sealed class TransactionListUiEvent: UiEvent {
    data class ShowTransactionInfo(val transaction: TransactionUi): TransactionListUiEvent()
    data class GetTransactionsByType(val transactionType: TransactionType?): TransactionListUiEvent()
}

sealed class TransactionInfoUiEvent: UiEvent {
    data class DeleteTransaction(val transaction: TransactionUi): TransactionInfoUiEvent()
}

sealed class WalletListUiEvent: UiEvent{
    data object CreateWallet: WalletListUiEvent()
    data class ShowWalletInfo(val wallet: WalletUi): WalletListUiEvent()
}

sealed class WalletInfoUiEvent: UiEvent {
    data class EditWallet(val wallet: WalletUi): WalletInfoUiEvent()
    data class DeleteWallet(val wallet: WalletUi): WalletInfoUiEvent()
}

sealed class AnalyticsUiEvent: UiEvent{
    data class GetAnalyticsByPeriod(val transactionType: TransactionType?, val period: AnalyticsPeriod): AnalyticsUiEvent()
    data class GetTransactionsByType(val transactionType: TransactionType?, val period: AnalyticsPeriod): AnalyticsUiEvent()
    data class ShowTransactionInfo(val transaction: TransactionUi): AnalyticsUiEvent()

}


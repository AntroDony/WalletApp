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

sealed class CreateWalletUiEvent{
    data class AddWallet(val wallet: WalletUi): CreateWalletUiEvent()
    data class UpdateWallet(val wallet: WalletUi): CreateWalletUiEvent()

    data object GoBack: CreateWalletUiEvent()
}

sealed class CreateTransactionUiEvent{
    data class AddTransaction(val transaction: TransactionUi): CreateTransactionUiEvent()
    data object CreateWallet: CreateTransactionUiEvent()

    data object GoBack: CreateTransactionUiEvent()
}

sealed class TransactionListUiEvent {
    data class ShowTransactionInfo(val transaction: TransactionUi): TransactionListUiEvent()
    data class GetTransactionsByType(val transactionType: TransactionType?): TransactionListUiEvent()

    data object GoBack: TransactionListUiEvent()
}

sealed class TransactionInfoUiEvent {
    data class DeleteTransaction(val transaction: TransactionUi): TransactionInfoUiEvent()

    data object GoBack: TransactionInfoUiEvent()
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
    data class FilterAnalyticsData(
        val type: TransactionType?,
        val category: TransactionCategoryUi?,
        val period: AnalyticsPeriod,
        val periodOffset: Int
    ): AnalyticsUiEvent()

    data class ShowTransactionInfo(val transaction: TransactionUi): AnalyticsUiEvent()

    data object GoBack: AnalyticsUiEvent()
}


package com.ancraz.mywallet.presentation.ui.events

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi

sealed interface UiEvent {
    data object GoBack: UiEvent
}

sealed class HomeScreenUiEvent: UiEvent{
    data class CreateTransaction(val transactionType: TransactionType): HomeScreenUiEvent()
    data class EditTotalBalance(val currentBalance: Float): HomeScreenUiEvent()
    data class EditWallet(val wallet: WalletUi): HomeScreenUiEvent()
    data object CreateWallet: HomeScreenUiEvent()
    data object SyncData: HomeScreenUiEvent()
}

sealed class EditBalanceScreenUiEvent: UiEvent{
    data class UpdateBalanceValue(val newBalance: Float): EditBalanceScreenUiEvent()
}

sealed class CreateWalletScreenUiEvent: UiEvent{
    data class AddWallet(val wallet: WalletUi): CreateWalletScreenUiEvent()
}

sealed class TransactionInputScreenUiEvent: UiEvent{
    data class AddTransaction(val transaction: TransactionUi): TransactionInputScreenUiEvent()
}


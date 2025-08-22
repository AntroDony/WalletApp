package com.ancraz.mywallet.presentation.ui.screens.editBalance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ancraz.mywallet.R
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.TransactionConfigContainer
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.events.EditBalanceUiEvent
import com.ancraz.mywallet.presentation.ui.screens.home.HomeViewModel
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun EditBalanceScreen(
    totalBalance: Float,
    paddingValues: PaddingValues,
    onEvent: (EditBalanceUiEvent) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    EditBalanceContainer(
        uiState = EditBalanceUiState(
            data = EditBalanceUiState.EditBalanceScreenData(
                currentTotalBalance = totalBalance.toFormattedString()
            )
        ),
        modifier = Modifier.padding(paddingValues),
        onEvent = { event ->
            when(event){
                is EditBalanceUiEvent.UpdateBalanceValue -> {
                    viewModel.editTotalBalance(event.newBalance)
                }
                else -> onEvent(event)
            }
        }
    )
}


@Composable
private fun EditBalanceContainer(
    uiState: EditBalanceUiState,
    modifier: Modifier,
    onEvent: (EditBalanceUiEvent) -> Unit
){
    val valueState = rememberSaveable { mutableStateOf(uiState.data.currentTotalBalance) }
    val currencyState = rememberSaveable { mutableStateOf(uiState.data.currency) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalSpacer()

        NavigationToolbar(
            title = stringResource(R.string.edit_balance_screen_title),
            onClickBack = {
                onEvent(EditBalanceUiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        TransactionConfigContainer(
            valueState = valueState,
            currencyState = currencyState,
            title = stringResource(R.string.edit_balance_total_balance_title),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )

        HorizontalSpacer()

        InputNumberKeyboard(
            valueState
        )

        HorizontalSpacer()

        ActionButton(
            title = stringResource(R.string.edit_balance_update_button),
            onClick = {
                val newBalanceValue = valueState.value.toFloatValue()
                onEvent(EditBalanceUiEvent.UpdateBalanceValue(newBalanceValue))
                onEvent(EditBalanceUiEvent.GoBack)
            }
        )
    }
}



@Preview
@Composable
fun EditBalancePreview() {
    MyWalletTheme {
        EditBalanceContainer(
            uiState = EditBalanceUiState(
                data = EditBalanceUiState.EditBalanceScreenData(
                    currentTotalBalance = "800.00"
                )
            ),
            modifier = Modifier,
            onEvent = {}
        )
    }
}
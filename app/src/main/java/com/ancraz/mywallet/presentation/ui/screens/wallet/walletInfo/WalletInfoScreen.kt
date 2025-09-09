package com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.DeleteDialog
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InfoRow
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.WalletInfoUiEvent
import com.ancraz.mywallet.presentation.ui.utils.getWalletCurrenciesString
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.errorColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun WalletInfoScreen(
    walletId: Long,
    paddingValues: PaddingValues,
    onEvent: (WalletInfoUiEvent) -> Unit,
    viewModel: WalletInfoViewModel = viewModel()
) {
    LaunchedEffect(walletId) {
        viewModel.getWalletById(walletId)
    }

    WalletInfoContainer(
        uiState = viewModel.walletUiState.collectAsStateWithLifecycle().value,
        modifier = Modifier.padding(paddingValues),
        onEvent = { event ->
            when(event){
                is WalletInfoUiEvent.DeleteWallet -> {
                    viewModel.deleteWallet(event.walletId)
                    onEvent(WalletInfoUiEvent.GoBack)
                }
                else -> {
                    onEvent(event)
                }
            }

        }
    )
}

@Composable
private fun WalletInfoContainer(
    uiState: WalletUiState,
    modifier: Modifier = Modifier,
    onEvent: (WalletInfoUiEvent) -> Unit
){
    val isDeleteDialogOpened = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {

        HorizontalSpacer()

        NavigationToolbar(
            title = stringResource(R.string.wallet_info_screen_title),
            onClickBack = {
                onEvent(WalletInfoUiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier.fillMaxSize()
            )
        } else if (uiState.wallet == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.AttachMoney,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(onBackgroundColor),
                        modifier = Modifier
                            .size(200.dp)
                    )

                    HorizontalSpacer()

                    Text(
                        text = stringResource(R.string.wallet_info_not_found_error_title),
                        color = onBackgroundColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }


        } else {
            if (isDeleteDialogOpened.value){
                DeleteDialog(
                    title = stringResource(R.string.delete_wallet_dialog_title),
                    text = stringResource(R.string.delete_wallet_dialog_text),
                    onConfirm = {
                        isDeleteDialogOpened.value = false
                        onEvent(WalletInfoUiEvent.DeleteWallet(uiState.wallet.id))
                    },
                    onDismiss = {
                        isDeleteDialogOpened.value = false
                    }
                )
            }


            InfoRow(
                title = stringResource(R.string.wallet_info_name_title),
                info = uiState.wallet.name
            )

            HorizontalSpacer()

            InfoRow(
                title = stringResource(R.string.wallet_info_type_title),
                info = uiState.wallet.walletType.walletName
            )

            HorizontalSpacer()

            uiState.wallet.description?.let {
                InfoRow(
                    title = stringResource(R.string.wallet_info_description_title),
                    info = uiState.wallet.description
                )

                HorizontalSpacer()
            }

            InfoRow(
                title = stringResource(R.string.wallet_info_total_balance_title),
                info = uiState.wallet.totalBalance
            )

            HorizontalSpacer()

            InfoRow(
                title = stringResource(R.string.wallet_info_currencies_title),
                info = uiState.wallet.getWalletCurrenciesString()
            )

            HorizontalSpacer()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(uiState.wallet.accounts) { account ->
                    InfoRow(
                        title = "${account.currency.name}:",
                        info = account.moneyValue,
                    )

                    HorizontalSpacer()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionButton(
                        title = stringResource(R.string.wallet_delete_button),
                        containerColor = errorColor,
                        contentColor = onBackgroundColor,
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            isDeleteDialogOpened.value = true
                        }
                    )

                    VerticalSpacer()

                    ActionButton(
                        title = stringResource(R.string.wallet_edit_button),
                        containerColor = primaryColor,
                        contentColor = onPrimaryColor,
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onEvent(WalletInfoUiEvent.EditWallet(uiState.wallet.id))
                        }
                    )
                }
            }
        }

        HorizontalSpacer()
    }
}


@Preview
@Composable
private fun WalletInfoScreenPreview() {
    MyWalletTheme {
        WalletInfoContainer(
            modifier = Modifier.background(backgroundColor),
            uiState = WalletUiState(
                isLoading = false,
                wallet = WalletUi(
                    name = "TBC Card",
                    description = "TBC Bank physic account",
                    walletType = WalletType.CARD,
                    accounts = getTestCurrencyAccountList(),
                    totalBalance = "2400 USD"
                ),
            ),
            onEvent = {}
        )
    }
}
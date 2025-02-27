package com.ancraz.mywallet.presentation.ui.screens.wallet.walletInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InfoRow
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.UiEvent
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
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun WalletInfoScreen(
    uiState: WalletUiState,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {

        HorizontalSpacer()

        NavigationToolbar(
            title = "Wallet Info",
            onClickBack = {
                onEvent(UiEvent.GoBack)
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
                        text = "Error...This wallet is not found",
                        color = onBackgroundColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }


        } else {
            InfoRow(
                title = "Wallet name:",
                info = uiState.wallet.name
            )

            HorizontalSpacer()

            InfoRow(
                title = "Wallet type:",
                info = uiState.wallet.walletType.walletName
            )

            HorizontalSpacer()

            uiState.wallet.description?.let {
                InfoRow(
                    title = "Description:",
                    info = uiState.wallet.description
                )

                HorizontalSpacer()
            }

            InfoRow(
                title = "Total balance:",
                info = "${uiState.wallet.totalBalance.toFormattedString()} USD"
            )

            HorizontalSpacer()

            InfoRow(
                title = "Wallet currencies:",
                info = uiState.wallet.getWalletCurrenciesString()
            )

            HorizontalSpacer()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(uiState.wallet.accounts) { account ->
                    InfoRow(
                        title = "${account.currency.name}:",
                        info = account.moneyValue.toFormattedString(),
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
                        title = "Delete",
                        containerColor = errorColor,
                        contentColor = onBackgroundColor,
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onEvent(WalletInfoUiEvent.DeleteWallet(uiState.wallet))
                        }
                    )

                    VerticalSpacer()

                    ActionButton(
                        title = "Edit",
                        containerColor = primaryColor,
                        contentColor = onPrimaryColor,
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onEvent(WalletInfoUiEvent.EditWallet(uiState.wallet))
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
        WalletInfoScreen(
            modifier = Modifier.background(backgroundColor),
            uiState = WalletUiState(
                isLoading = false,
                wallet = WalletUi(
                    name = "TBC Card",
                    description = "TBC Bank physic account",
                    walletType = WalletType.CARD,
                    accounts = listOf(
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.USD, 2000f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.GEL, 567.20f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.RUB, 2000f)
                    ),
                    totalBalance = 2400f
                ),
            ),
            onEvent = {}
        )
    }
}
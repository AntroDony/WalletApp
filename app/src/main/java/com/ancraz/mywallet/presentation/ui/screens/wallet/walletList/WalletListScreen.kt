package com.ancraz.mywallet.presentation.ui.screens.wallet.walletList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.CreateWalletButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.WalletListUiEvent
import com.ancraz.mywallet.presentation.ui.utils.getImageByWalletType
import com.ancraz.mywallet.presentation.ui.utils.getWalletCurrenciesString
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun WalletListScreen(
    uiState: WalletListUiState,
    modifier: Modifier = Modifier,
    onEvent: (WalletListUiEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {

        HorizontalSpacer()

        NavigationToolbar(
            title = stringResource(R.string.wallet_list_screen_title),
            onClickBack = {
                onEvent(WalletListUiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else if (uiState.walletList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Rounded.List,
                    contentDescription = "Empty list",
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = Modifier
                        .size(200.dp)
                )

                HorizontalSpacer()

                Text(
                    text = stringResource(R.string.empty_wallet_list_text),
                    color = onBackgroundColor,
                    fontSize = 18.sp
                )

                HorizontalSpacer()

                CreateWalletButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = {
                        onEvent(WalletListUiEvent.CreateWallet)
                    }
                )
            }
        }
        else {
            LazyColumn {
                items(uiState.walletList){ wallet ->
                    WalletCard(
                        wallet = wallet,
                        onClick = {
                            onEvent(WalletListUiEvent.ShowWalletInfo(wallet))
                        }
                    )

                    HorizontalSpacer()
                }

                item {
                    CreateWalletButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onEvent(WalletListUiEvent.CreateWallet)
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun WalletCard(
    wallet: WalletUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(width = 1.dp, color = primaryColor),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = wallet.walletType.getImageByWalletType(),
                    contentDescription = wallet.walletType.walletName,
                    tint = primaryColor,
                    modifier = Modifier
                        .size(40.dp)
                )

                VerticalSpacer()

                Text(
                    text = wallet.name,
                    color = onBackgroundColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier
                )
                VerticalSpacer()
            }

            HorizontalSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.wallet_card_balance_title),
                    color = onBackgroundColor.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )

                VerticalSpacer()

                Text(
                    text = wallet.totalBalance,
                    color = onBackgroundColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.wallet_card_currencies_title),
                    color = onBackgroundColor.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )

                VerticalSpacer()

                Text(
                    text = wallet.getWalletCurrenciesString(),
                    color = onBackgroundColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview
@Composable
private fun WalletListScreenPreview(){
    MyWalletTheme {
        WalletListScreen(
            modifier = Modifier.background(backgroundColor),
            uiState = WalletListUiState(
                walletList = listOf(
                    WalletUi(
                        name = "TBC Card",
                        description = "TBC Bank physic account",
                        walletType = WalletType.CARD,
                        accounts = getTestCurrencyAccountList(),
                        totalBalance = "2400 USD"
                    ),
                    WalletUi(
                        name = "Cash",
                        description = "TBC Bank physic account",
                        walletType = WalletType.CASH,
                        accounts = getTestCurrencyAccountList(),
                        totalBalance = "2400 USD"
                    ),
                    WalletUi(
                        name = "Trust Wallet 1",
                        walletType = WalletType.CRYPTO_WALLET,
                        accounts = getTestCurrencyAccountList(),
                        totalBalance = "2400 USD"
                    )
                )
            ),
            onEvent = {}
        )
    }
}
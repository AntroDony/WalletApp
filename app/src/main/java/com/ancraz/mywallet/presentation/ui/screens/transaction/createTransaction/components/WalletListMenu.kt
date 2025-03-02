package com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.CreateWalletButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.theme.surfaceColor
import com.ancraz.mywallet.presentation.ui.utils.getImageByWalletType
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString


@Composable
fun WalletListMenu(
    wallets: List<WalletUi>,
    modifier: Modifier = Modifier,
    onEvent: (CreateTransactionUiEvent) -> Unit,
    onSelect: (WalletUi) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                .background(surfaceColor)

        ) {
            Image(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Close",
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
                    .clickable {
                        onClose()
                    }
            )
        }

        if (wallets.isEmpty()){
            Column(
                modifier = Modifier
                    .background(surfaceColor)
            ) {
                HorizontalSpacer()
                CreateWalletButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenHorizontalPadding),
                    onClick = {
                        onEvent(CreateTransactionUiEvent.CreateWallet)
                    }
                )
                HorizontalSpacer()
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .background(surfaceColor)
            ) {
                items(wallets) { wallet ->
                    WalletItem(
                        wallet,
                        modifier = Modifier
                            .clickable {
                                onSelect(wallet)
                            }
                    )
                }
            }
        }
    }
}


@Composable
private fun WalletItem(
    wallet: WalletUi,
    modifier: Modifier
){
    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(width = 1.dp, color = primaryColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier
                    //.weight(1f)
                )

                VerticalSpacer()
            }

            HorizontalSpacer(height = 8.dp)

            Text(
                text = "Balance",
                color = onBackgroundColor.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            HorizontalSpacer(height = 8.dp)

            Text(
                text = "${wallet.totalBalance.toFormattedString()} USD",
                color = onBackgroundColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
private fun WalletListMenuPreview(){
    MyWalletTheme {
        WalletListMenu(
            wallets = listOf(
                WalletUi(
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
                WalletUi(
                    name = "Cash",
                    description = "TBC Bank physic account",
                    walletType = WalletType.CASH,
                    accounts = listOf(
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.USD, 2000f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.GEL, 567.20f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.RUB, 2000f)
                    ),
                    totalBalance = 2400f
                ),
                WalletUi(
                    name = "Trust Wallet 1",
                    walletType = WalletType.CRYPTO_WALLET,
                    accounts = listOf(
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.USD, 2000f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.GEL, 567.20f),
                        WalletUi.CurrencyAccountUi(currency = CurrencyCode.RUB, 2000f)
                    ),
                    totalBalance = 2400f
                )
            ),
            onEvent = {},
            onSelect = {},
            onClose = {}
        )
    }
}


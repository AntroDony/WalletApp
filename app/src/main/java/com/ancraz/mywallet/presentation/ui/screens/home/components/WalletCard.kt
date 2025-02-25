package com.ancraz.mywallet.presentation.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun WalletCard(
    wallet: WalletUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentWidth()
            .widthIn(max = 300.dp)
            .padding(vertical = 10.dp),
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
                .wrapContentWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = getImageByWalletType(wallet.walletType),
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
private fun WalletCardPreview(){
    MyWalletTheme {
        WalletCard(
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
            onClick = {}
        )
    }
}


private fun getImageByWalletType(walletType: WalletType): ImageVector{
    return when(walletType){
        WalletType.CARD -> Icons.Filled.CreditCard
        WalletType.CASH -> Icons.Filled.Money
        WalletType.BANK_ACCOUNT -> Icons.Filled.AccountBalance
        WalletType.CRYPTO_WALLET -> Icons.Filled.CurrencyBitcoin
        WalletType.INVESTMENTS -> Icons.Filled.WaterfallChart
        WalletType.OTHER -> Icons.Filled.Payments
    }
}
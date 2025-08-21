package com.ancraz.mywallet.presentation.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.utils.getImageByWalletType
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList
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
                )

                VerticalSpacer()
            }

            HorizontalSpacer(height = 8.dp)

            Text(
                text = stringResource(R.string.wallet_balance_title),
                color = onBackgroundColor.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            HorizontalSpacer(height = 8.dp)

            Text(
                text = wallet.totalBalance,
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
                accounts = getTestCurrencyAccountList(),
                totalBalance = "2400 USD"
            ),
            onClick = {}
        )
    }
}
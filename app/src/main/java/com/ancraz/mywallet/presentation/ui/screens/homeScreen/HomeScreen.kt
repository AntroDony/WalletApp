package com.ancraz.mywallet.presentation.ui.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.states.HomeScreenData
import com.ancraz.mywallet.presentation.states.HomeScreenState
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import java.util.Calendar

@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState,
    modifier: Modifier = Modifier,
    onMadeTransaction: (TransactionType) -> Unit,
    onEditBalance: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        HorizontalSpacer()

        Text(
            text = "My Wallet",
            color = onSurfaceColor,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        HorizontalSpacer()

        TotalBalanceCard(
            homeScreenState = homeScreenState,
            onNewTransaction = onMadeTransaction,
            onEditBalance = onEditBalance
        )
    }

}


@Composable
private fun TotalBalanceCard(
    homeScreenState: HomeScreenState,
    modifier: Modifier = Modifier,
    onNewTransaction: (TransactionType) -> Unit,
    onEditBalance: (Float) -> Unit
) {
    Card(
        modifier = modifier
            .padding(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "Total balance",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (homeScreenState.isLoading) {
                CircularProgressIndicator(
                    trackColor = onPrimaryColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "\$ ${homeScreenState.data.balance.toFormattedString()}",
                    color = Color.Black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TotalBalanceActionButton(
                    text = "Income",
                    icon = Icons.Filled.Add
                ) {
                    onNewTransaction(TransactionType.INCOME)
                }

                TotalBalanceActionButton(
                    text = "Expense",
                    icon = Icons.Filled.Remove
                ) {
                    onNewTransaction(TransactionType.EXPENSE)
                }

                TotalBalanceActionButton(
                    text = "Edit",
                    icon = Icons.Filled.Edit
                ) {
                    onEditBalance(homeScreenState.data.balance)
                }
            }

        }
    }
}

@Composable
private fun TotalBalanceActionButton(
    text: String,
    icon: ImageVector,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(secondaryColor)
                .clickable {
                    onAction()
                }
        ) {
            Image(
                imageVector = icon,
                contentDescription = text,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .padding(12.dp)
                    .size(26.dp)
            )
        }

        Text(
            text = text,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    MyWalletTheme {
        HomeScreen(
            homeScreenState = HomeScreenState(
                isLoading = true,
                data = HomeScreenData(
                    balance = 8000f,
                    transactions = listOf(
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.EXPENSE, currency = CurrencyCode.USD),
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.INCOME, currency = CurrencyCode.USD),
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.EXPENSE, currency = CurrencyCode.USD),
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.EXPENSE, currency = CurrencyCode.USD),
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.EXPENSE, currency = CurrencyCode.USD),
                        TransactionUi(time = Calendar.getInstance().timeInMillis, value = 200f, type = TransactionType.EXPENSE, currency = CurrencyCode.USD),
                    )
                )
            ),
            onMadeTransaction = {},
            onEditBalance = {}
        )
    }
}
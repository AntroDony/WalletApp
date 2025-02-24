package com.ancraz.mywallet.presentation.ui.screens.homeScreen

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.states.HomeScreenState
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.TransactionCard
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.components.WalletCard
import com.ancraz.mywallet.presentation.ui.events.HomeScreenUiEvent
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import java.util.Calendar

@Composable
fun HomeScreen(
    uiState: HomeScreenState,
    modifier: Modifier,
    onEvent: (HomeScreenUiEvent) -> Unit
) {
    debugLog("HomeScreen state: $uiState")

    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {
        HorizontalSpacer()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Text(
                text = "My Wallet",
                color = onSurfaceColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            Image(
                imageVector = Icons.Outlined.Sync,
                contentDescription = "Sync data",
                colorFilter = ColorFilter.tint(onSurfaceColor),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        onEvent(HomeScreenUiEvent.SyncData)
                    }
            )
        }

        HorizontalSpacer()

        TotalBalanceCard(
            state = uiState,
            onNewTransaction = { transactionType ->
                onEvent(
                    HomeScreenUiEvent.CreateTransaction(transactionType)
                )
                               },
            onEditBalance = { currentBalance ->
                onEvent(
                    HomeScreenUiEvent.EditTotalBalance(currentBalance)
                )
            }
        )

        HorizontalSpacer()

        WalletListContainer(
            wallets = uiState.data.wallets,
            onEditWallet = { wallet ->
                onEvent(
                    HomeScreenUiEvent.EditWallet(wallet)
                )
            },
            onCreateWallet = {
                onEvent(HomeScreenUiEvent.CreateWallet)
            }
        )

        HorizontalSpacer()

        TransactionListContainer(
            transactions = uiState.data.transactions
        )
    }

}


@Composable
private fun TotalBalanceCard(
    state: HomeScreenState,
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
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (state.isLoading) {
                CircularProgressIndicator(
                    trackColor = onPrimaryColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "\$ ${state.data.balance.toFormattedString()}",
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
                    onEditBalance(state.data.balance)
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


@Composable
private fun WalletListContainer(
    wallets: List<WalletUi>,
    modifier: Modifier = Modifier,
    onEditWallet: (WalletUi) -> Unit,
    onCreateWallet: () -> Unit
){
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Wallets",
                fontSize = 16.sp,
                color = onBackgroundColor
            )

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All",
                    fontSize = 16.sp,
                    color = onBackgroundColor
                )

                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "All Wallets",
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }

        if (wallets.isEmpty()){
            AddNewWalletButton(
                onClick = {
                    onCreateWallet()
                }
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),

            ) {
                items(wallets){ wallet ->
                    WalletCard(
                        wallet = wallet,
                        onClick = {
                            onEditWallet(wallet)
                        }
                    )

                    VerticalSpacer()
                }
            }
        }
    }
}


@Composable
private fun AddNewWalletButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        val stroke = Stroke(width = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
        Canvas(Modifier.fillMaxWidth().height(70.dp)){
            drawRoundRect(
                color = primaryColor,
                style = stroke,
                cornerRadius = CornerRadius(x = 40f, y = 40f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Wallet",
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .size(34.dp)
            )

            VerticalSpacer()

            Text(
                text = "Add Wallet",
                color = onSecondaryColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun TransactionListContainer(
    transactions: List<TransactionUi>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Transactions",
                fontSize = 16.sp,
                color = onBackgroundColor
            )

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All",
                    fontSize = 16.sp,
                    color = onBackgroundColor
                )

                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "All Transactions",
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }

        if (transactions.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = Icons.AutoMirrored.Rounded.List,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(onSurfaceColor),
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Text(
                        text = "No transactions",
                        color = onSurfaceColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

            }
        } else {
            LazyColumn(
                modifier = Modifier
            ){
                items(transactions){ transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = {}
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    MyWalletTheme {
        HomeScreen(
            uiState = HomeScreenState(
                isLoading = true,
                data = HomeScreenState.HomeScreenData(
                    balance = 8000f,
                    transactions = listOf(
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.EXPENSE,
                            currency = CurrencyCode.USD,
                            description = "Transaction 1"
                        ),
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.INCOME,
                            currency = CurrencyCode.USD,
                            description = "Transaction 2"
                        ),
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.EXPENSE,
                            currency = CurrencyCode.USD,
                            description = "Transaction 3"
                        ),
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.EXPENSE,
                            currency = CurrencyCode.USD,
                            description = "Transaction 4"
                        ),
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.EXPENSE,
                            currency = CurrencyCode.USD,
                            description = "Transaction 5"
                        ),
                        TransactionUi(
                            time = Calendar.getInstance().timeInMillis,
                            value = 200f,
                            type = TransactionType.EXPENSE,
                            currency = CurrencyCode.USD,
                            description = "Transaction 6"
                        ),
                    ),
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
                    )
                )
            ),
            modifier = Modifier,
            onEvent = {}
        )
    }
}
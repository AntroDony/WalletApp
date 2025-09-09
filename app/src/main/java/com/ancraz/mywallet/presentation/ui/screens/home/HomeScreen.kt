package com.ancraz.mywallet.presentation.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.CreateWalletButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.TransactionCard
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.screens.home.components.WalletCard
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.screens.home.components.TotalBalanceCard
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList
import java.util.Calendar

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onEvent: (HomeUiEvent) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    HomeContainer(
        uiState = viewModel.homeUiState.collectAsStateWithLifecycle().value,
        modifier = Modifier.padding(paddingValues),
        onEvent = { event ->
            when (event) {
                is HomeUiEvent.SyncData -> {
                    viewModel.syncData()
                }

                is HomeUiEvent.ChangePrivateMode -> {
                    viewModel.changePrivateMode(event.isPrivate)
                }

                else -> {
                    onEvent(event)
                }
            }
        }
    )
}


@Composable
private fun HomeContainer(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {
    val context = LocalContext.current

    uiState.error?.let {
        Toast.makeText(
            context,
            stringResource(R.string.currency_rates_loading_error_message),
            Toast.LENGTH_LONG
        ).show()
    }

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
                text = stringResource(R.string.home_screen_title),
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
                        onEvent(HomeUiEvent.SyncData)
                    }
            )
        }

        HorizontalSpacer()

        TotalBalanceCard(
            state = uiState,
            onEvent = { event ->
                onEvent(event)
            }
        )

        HorizontalSpacer()

        WalletListContainer(
            wallets = uiState.data.wallets,
            onEvent = onEvent
        )

        HorizontalSpacer()

        TransactionListContainer(
            transactions = uiState.data.transactions,
            onEvent = onEvent
        )
    }

}

@Composable
private fun WalletListContainer(
    wallets: List<WalletUi>,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {
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
                text = stringResource(R.string.home_wallet_list_title),
                fontSize = 16.sp,
                color = onBackgroundColor
            )

            Row(
                modifier = Modifier
                    .clickable {
                        onEvent(HomeUiEvent.ShowAllWallets)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.home_all_button),
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

        if (wallets.isEmpty()) {
            HorizontalSpacer(height = 10.dp)

            CreateWalletButton(
                onClick = {
                    onEvent(HomeUiEvent.CreateWallet)
                }
            )

            HorizontalSpacer(height = 10.dp)
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),

                ) {
                items(wallets) { wallet ->
                    WalletCard(
                        wallet = wallet,
                        onClick = {
                            onEvent(HomeUiEvent.ShowWalletInfo(wallet))
                        }
                    )

                    VerticalSpacer()
                }
            }
        }
    }
}

@Composable
private fun TransactionListContainer(
    transactions: List<TransactionUi>,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {
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
                text = stringResource(R.string.home_transaction_list_title),
                fontSize = 16.sp,
                color = onBackgroundColor
            )

            Row(
                modifier = Modifier
                    .clickable {
                        onEvent(HomeUiEvent.ShowAllTransactions)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_all_button),
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

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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
                        text = stringResource(R.string.home_empty_transaction_list_placeholder),
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
            ) {
                items(transactions) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = {
                            onEvent(
                                HomeUiEvent.ShowTransactionInfo(transaction)
                            )
                        }
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
        HomeContainer(
            uiState = HomeUiState(
                isLoading = true,
                data = HomeUiState.HomeScreenData(
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
                )
            ),
            modifier = Modifier.background(backgroundColor),
            onEvent = {}
        )
    }
}
package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.TransactionCard
import com.ancraz.mywallet.presentation.ui.components.TransactionTypeSelector
import com.ancraz.mywallet.presentation.ui.events.TransactionListUiEvent
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import java.util.Calendar

@Composable
fun TransactionListScreen(
    paddingValues: PaddingValues,
    onEvent: (TransactionListUiEvent) -> Unit,
    viewModel: TransactionListViewModel = viewModel()
) {

    TransactionListContainer(
        uiState = viewModel.transactionListUiState.collectAsStateWithLifecycle().value,
        modifier = Modifier.padding(paddingValues),
        onEvent = { event ->
            when(event){
                is TransactionListUiEvent.FilterTransactionByType -> {
                    viewModel.filterTransactionsByType(event.transactionType)
                }
                else -> {
                    onEvent(event)
                }
            }
        }
    )
}


@Composable
private fun TransactionListContainer(
    uiState: TransactionListUiState,
    modifier: Modifier = Modifier,
    onEvent: (TransactionListUiEvent) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {

        HorizontalSpacer()

        NavigationToolbar(
            title = stringResource(R.string.transaction_list_screen_title),
            onClickBack = {
                onEvent(TransactionListUiEvent.GoBack)
            }
        )

        HorizontalSpacer(height = 30.dp)

        TransactionTypeSelector(
            selectedType = uiState.filteredType,
            onTypeSelected = { type ->
                TransactionListUiEvent.FilterTransactionByType(type)
            }
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else if (uiState.transactionList.isEmpty()) {
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
                    text = stringResource(R.string.transaction_list_empty_list_title),
                    color = onBackgroundColor,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn {
                items(uiState.transactionList) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = {
                            onEvent(
                                TransactionListUiEvent.ShowTransactionInfo(
                                    transaction
                                )
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
private fun TransactionListContainerPreview() {
    MyWalletTheme {
        TransactionListContainer(
            modifier = Modifier.background(backgroundColor),
            uiState = TransactionListUiState(
                isLoading = false,
                transactionList = listOf(
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
                )
            ),
            onEvent = {}
        )
    }
}
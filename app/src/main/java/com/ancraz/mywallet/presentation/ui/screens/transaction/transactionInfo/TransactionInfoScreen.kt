package com.ancraz.mywallet.presentation.ui.screens.transaction.transactionInfo

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.DeleteDialog
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InfoRow
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.TransactionInfoUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.errorColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.timeToString
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import java.util.Calendar

@Composable
fun TransactionInfoScreen(
    uiState: TransactionInfoUiState,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit
) {

    val isDeleteDialogOpened = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {

        HorizontalSpacer()

        NavigationToolbar(
            title = stringResource(R.string.transaction_info_screen_title),
            onClickBack = {
                onEvent(UiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier.fillMaxSize()
            )
        } else if (uiState.transaction == null) {
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
                        text = stringResource(R.string.transaction_info_error_title),
                        color = onBackgroundColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }


        } else {

            if (isDeleteDialogOpened.value){
                DeleteDialog(
                    title = stringResource(R.string.delete_transaction_dialog_title),
                    text = stringResource(R.string.delete_transaction_dialog_text),
                    onConfirm = {
                        isDeleteDialogOpened.value = false
                        onEvent(
                            TransactionInfoUiEvent.DeleteTransaction(
                                transaction = uiState.transaction
                            )
                        )
                    },
                    onDismiss = {
                        isDeleteDialogOpened.value = false
                    }
                )
            }

            InfoRow(
                title = stringResource(R.string.transaction_info_type_title),
                info = uiState.transaction.type.name
            )

            HorizontalSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.transaction_info_value_title),
                    color = primaryColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                VerticalSpacer()

                TransactionValueText(
                    transaction = uiState.transaction
                )
            }

            HorizontalSpacer()

            InfoRow(
                title = stringResource(R.string.transaction_info_time_title),
                info = uiState.transaction.time.timeToString()
            )

            HorizontalSpacer()

            uiState.transaction.description?.let { desc ->
                InfoRow(
                    title = stringResource(R.string.transaction_info_description_title),
                    info = desc
                )

                HorizontalSpacer()
            }


            uiState.transaction.category?.let { category ->
                InfoRow(
                    title = stringResource(R.string.transaction_info_category_title),
                    info = category.name
                )

                HorizontalSpacer()
            }

            uiState.transaction.wallet?.let { wallet ->
                InfoRow(
                    title = stringResource(R.string.transaction_info_wallet_title),
                    info = wallet.name
                )

                HorizontalSpacer()
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
                        title = stringResource(R.string.transaction_info_delete_button),
                        containerColor = errorColor,
                        contentColor = onBackgroundColor,
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            isDeleteDialogOpened.value = true
                        }
                    )
                }
            }
        }

        HorizontalSpacer()

    }
}


@Composable
private fun TransactionValueText(
    transaction: TransactionUi,
    modifier: Modifier = Modifier
) {
    val textColor = if (transaction.type == TransactionType.INCOME) primaryColor else errorColor
    val valuePrefix = if (transaction.type == TransactionType.INCOME) {
        "+"
    } else if (transaction.type == TransactionType.EXPENSE) {
        "-"
    } else {
        ""
    }

    val valueSuffix = transaction.currency.name

    Text(
        text = "$valuePrefix ${transaction.value.toFormattedString()} $valueSuffix",
        color = textColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}


@Preview
@Composable
private fun TransactionInfoScreenPreview() {
    MyWalletTheme {
        TransactionInfoScreen(
            modifier = Modifier.background(backgroundColor),
            uiState = TransactionInfoUiState(
                isLoading = false,
                transaction = TransactionUi(
                    time = Calendar.getInstance().timeInMillis,
                    value = 200f,
                    type = TransactionType.EXPENSE,
                    currency = CurrencyCode.USD,
                    description = "Transaction 1",
                    wallet = WalletUi(
                        name = "TBC Card",
                        walletType = WalletType.CARD,
                        totalBalance = 8000f,
                        accounts = emptyList()
                    )
                ),
            ),
            onEvent = {}
        )
    }
}

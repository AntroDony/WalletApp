package com.ancraz.mywallet.presentation.ui.screens.wallet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString


@Composable
fun CurrencyAccountList(
    accountList: MutableState<List<WalletUi.CurrencyAccountUi>>,
    modifier: Modifier = Modifier,
) {
    val isSelectAccountCurrencyDialogOpen = remember { mutableStateOf(false) }

    if (isSelectAccountCurrencyDialogOpen.value) {
        SelectNewAccountCurrencyDialog(
            currencies = CurrencyCode.entries,
            onResult = { currency ->
                accountList.value = accountList.value
                    .toMutableList()
                    .apply {
                        add(
                            WalletUi.CurrencyAccountUi(
                                currency = currency
                            )
                        )
                    }
                isSelectAccountCurrencyDialogOpen.value = false
            },
            onCancel = {
                isSelectAccountCurrencyDialogOpen.value = false
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(accountList.value) { index, account ->
            CurrencyAccountItem(
                account = account,
                onEditAccount = { editedAccount ->
                    accountList.value = accountList.value
                        .toMutableList()
                        .apply {
                            set(index, editedAccount)
                        }
                },
                onDelete = { account ->
                    accountList.value = accountList.value
                        .toMutableList()
                        .apply {
                            remove(account)
                        }
                }
            )
        }
        item {
            AddAccountButton(
                onClick = {
                    isSelectAccountCurrencyDialogOpen.value = true
                }
            )
        }
    }
}


@Composable
private fun CurrencyAccountItem(
    account: WalletUi.CurrencyAccountUi,
    modifier: Modifier = Modifier,
    onEditAccount: (WalletUi.CurrencyAccountUi) -> Unit,
    onDelete: (WalletUi.CurrencyAccountUi) -> Unit
) {
    val isEditValueDialogOpen = remember { mutableStateOf(false) }

    if (isEditValueDialogOpen.value) {
        EditAccountValueDialog(
            value = account.moneyValue,
            onResult = { newValueString ->
                onEditAccount(
                    account.copy(
                        moneyValue = newValueString
                    )
                )
                isEditValueDialogOpen.value = false
            },
            onCancel = {
                isEditValueDialogOpen.value = false
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(width = 1.dp, color = primaryColor),
        onClick = {
            isEditValueDialogOpen.value = true
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = account.currency.name,
                color = onBackgroundColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            VerticalSpacer(width = 20.dp)

            Text(
                text = account.moneyValue.toFormattedString(),
                color = onBackgroundColor,
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
            )

            VerticalSpacer(width = 20.dp)

            Row {
                Image(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = Modifier
                        .size(24.dp)
                )

                VerticalSpacer(width = 20.dp)

                Image(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable {
                            onDelete(account)
                        }
                )
            }
        }
    }
}
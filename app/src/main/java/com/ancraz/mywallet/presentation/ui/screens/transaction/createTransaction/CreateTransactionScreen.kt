package com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.CurrencyRateUi
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.TransactionConfigContainer
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.CreateTransactionUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components.CategoryListMenu
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components.WalletListMenu
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryContainerColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun CreateTransactionScreen(
    uiState: CreateTransactionUiState,
    transactionType: TransactionType,
    modifier: Modifier,
    onEvent: (UiEvent) -> Unit
) {
    debugLog("CreateTransactionScreen lastWalletId: ${uiState.data.lastUsedWalletId}")
    
    val context = LocalContext.current

    val inputValueState = remember { mutableStateOf(0f.toFormattedString()) }
    val currencyState = remember { mutableStateOf(CurrencyCode.USD) }

    val descriptionState = remember { mutableStateOf<String?>(null) }

    val isCategoryListOpen = remember { mutableStateOf(false) }
    val isWalletListOpen = remember { mutableStateOf(false) }
    val isWalletAccountsDialogOpen = remember { mutableStateOf(false) }

    val selectedWalletState = remember {
        mutableStateOf(
            uiState.data.lastUsedWalletId?.let { walletId ->
                uiState.data.walletList.find { wallet ->
                    wallet.id == walletId
                }
            }
        )
    }
    val selectedWalletCurrencyAccount = remember {
        mutableStateOf(selectedWalletState.value?.accounts?.getOrNull(0))
    }

    LaunchedEffect(selectedWalletState.value) {
        selectedWalletCurrencyAccount.value = selectedWalletState.value?.accounts?.getOrNull(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {
        HorizontalSpacer()

        NavigationToolbar(
            title = transactionType.name.lowercase().replaceFirstChar { it.uppercase() },
            onClickBack = {
                onEvent(UiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (isWalletAccountsDialogOpen.value) {
            selectedWalletState.value?.let { selectedWallet ->
                selectedWalletCurrencyAccount.value?.let { selectedAccount ->
                    SelectWalletAccountDialog(
                        accounts = selectedWallet.accounts,
                        currentAccount = selectedAccount,
                        onSelect = { account ->
                            selectedWalletCurrencyAccount.value = account

                            isWalletAccountsDialogOpen.value = false
                            isWalletListOpen.value = false
                        },
                        onCancel = {
                            isWalletAccountsDialogOpen.value = false
                        }
                    )
                }
            }
        }

        Column {
            TransactionConfigContainer(
                valueState = inputValueState,
                currencyState = currencyState,
                title = "Total balance: ${uiState.data.totalBalance.toFormattedString()}",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )

            HorizontalSpacer()

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        isWalletListOpen.value = true
                    }
            ) {
                Text(
                    text = getSelectedWalletInfoString(
                        selectedWalletState.value,
                        selectedWalletCurrencyAccount.value
                    ).uppercase(),
                    color = primaryColor,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        HorizontalSpacer()

        if (currencyState.value != CurrencyCode.USD) {
            RateInfoText(
                currentCurrencyState = currencyState.value,
                rates = uiState.data.currencyRates
            )
            HorizontalSpacer()
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (!isCategoryListOpen.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TransactionDescriptionTextField(descriptionState)

                    HorizontalSpacer()

                    InputNumberKeyboard(inputValueState)

                    HorizontalSpacer()

                    ActionButton(
                        title = "Select category",
                        onClick = {
                            isCategoryListOpen.value = true
                        }
                    )

                    HorizontalSpacer()
                }
            } else {
                val categoryList = if (transactionType == TransactionType.INCOME) {
                    uiState.data.incomeCategories
                } else {
                    uiState.data.expenseCategories
                }

                CategoryListMenu(
                    categories = categoryList,
                    onSelect = { category ->
                        buildTransactionObject(
                            value = inputValueState.value.toFloatValue(),
                            currency = currencyState.value,
                            type = transactionType,
                            description = descriptionState.value ?: category.name,
                            category = category,
                            wallet = selectedWalletState.value,
                            selectedAccount = selectedWalletCurrencyAccount.value,
                            onSuccess = { transactionObject ->
                                debugLog("Transaction: $transactionObject")

                                onEvent(
                                    CreateTransactionUiEvent.AddTransaction(transactionObject)
                                )
                                onEvent(UiEvent.GoBack)
                            },
                            onError = { message ->
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        )
                    },
                    onClose = {
                        isCategoryListOpen.value = false
                    }
                )
            }


            if (isWalletListOpen.value) {
                WalletListMenu(
                    wallets = uiState.data.walletList,
                    onSelect = { wallet ->
                        selectedWalletState.value = wallet

                        isWalletAccountsDialogOpen.value = true
                    },
                    onClose = {
                        isWalletListOpen.value = false
                    }
                )
            }
        }
    }
}


@Composable
private fun RateInfoText(
    currentCurrencyState: CurrencyCode,
    rates: List<CurrencyRateUi>,
    modifier: Modifier = Modifier
) {
    val baseText = "1 USD = "
    val currentRateIndex = rates.map { rate -> rate.currencyCode }.indexOf(currentCurrencyState)
    val rateText = "${rates[currentRateIndex].rate} ${currentCurrencyState.name}"
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = baseText + rateText,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = onSurfaceColor,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
private fun TransactionDescriptionTextField(
    textState: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(fontSize = 16.sp)

    OutlinedTextField(
        value = textState.value ?: "",
        onValueChange = {
            textState.value = it
        },
        maxLines = 1,
        textStyle = textStyle,
        placeholder = {
            Text(
                text = "Description",
                fontSize = 16.sp
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,

            focusedTextColor = primaryColor,
            unfocusedTextColor = primaryColor,

            cursorColor = primaryColor,
            focusedPlaceholderColor = primaryContainerColor,
            unfocusedPlaceholderColor = primaryContainerColor,

            focusedIndicatorColor = primaryColor,
            unfocusedIndicatorColor = primaryContainerColor,

            ),
        modifier = modifier
            .fillMaxWidth()
    )
}


@Composable
private fun SelectWalletAccountDialog(
    accounts: List<WalletUi.CurrencyAccountUi>,
    currentAccount: WalletUi.CurrencyAccountUi,
    modifier: Modifier = Modifier,
    onSelect: (WalletUi.CurrencyAccountUi) -> Unit,
    onCancel: () -> Unit
) {
    val selectedAccountState = remember { mutableStateOf(currentAccount) }

    Dialog(
        onDismissRequest = {
            onCancel()
        }
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(width = 1.dp, color = primaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Select currency account",
                    color = onBackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(height = 20.dp)

                Column(
                    modifier = Modifier
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    accounts.forEach { account ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = account == selectedAccountState.value,
                                onClick = {
                                    selectedAccountState.value = account
                                }
                            )

                            VerticalSpacer()

                            Text(
                                text = "${account.moneyValue.toFormattedString()} ${account.currency}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = onBackgroundColor
                            )
                        }
                    }
                }

                HorizontalSpacer(height = 20.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onCancel() },
                    ) {
                        Text(
                            text = "Cancel",
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    TextButton(
                        onClick = {
                            onSelect(selectedAccountState.value)
                        }
                    ) {
                        Text(
                            text = "Save",
                            color = onBackgroundColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun TransactionInputScreenPreview() {
    MyWalletTheme {
        CreateTransactionScreen(
            uiState = CreateTransactionUiState(
                data = CreateTransactionUiState.TransactionScreenData(
                    totalBalance = 5000f,
                    currencyRates = listOf(
                        CurrencyRateUi(CurrencyCode.EUR, 1.2f),
                        CurrencyRateUi(CurrencyCode.KZT, 0.11f),
                        CurrencyRateUi(CurrencyCode.RUB, 99f),
                        CurrencyRateUi(CurrencyCode.GEL, 0.4f),
                    ),
                    expenseCategories = listOf(
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 2",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 3",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 4",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 2",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 3",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 4",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 2",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 3",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 4",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 5",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.EXPENSE
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                        TransactionCategoryUi(
                            name = "Category 1",
                            iconAssetPath = "categories_icon/house_category.svg",
                            transactionType = TransactionType.INCOME
                        ),
                    ),
                    walletList = listOf(
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
            TransactionType.INCOME,
            modifier = Modifier.background(backgroundColor),
            onEvent = {}
        )
    }
}

private fun buildTransactionObject(
    value: Float,
    currency: CurrencyCode,
    type: TransactionType,
    description: String?,
    category: TransactionCategoryUi,
    wallet: WalletUi?,
    selectedAccount: WalletUi.CurrencyAccountUi?,
    onSuccess: (TransactionUi) -> Unit,
    onError: (String) -> Unit
) {

    if (value == 0f) {
        onError("Transaction value cannot be 0")
        return
    }
    selectedAccount?.let { account ->
        if (account.moneyValue < value) {
            onError(" You are in a minus. Not enough money on selected account")
           // return
        } else if (selectedAccount.currency != currency) {
            onError("Selected currency is incompatible with selected account")
            return
        }
    }

    onSuccess(
        TransactionUi(
            value = value,
            currency = currency,
            type = type,
            description = description,
            category = category,
            wallet = wallet,
            selectedWalletAccount = selectedAccount
        )
    )
}


private fun getSelectedWalletInfoString(
    wallet: WalletUi?,
    selectedAccount: WalletUi.CurrencyAccountUi?
): String {
    if (wallet == null)
        return "No wallet"

    val currencyText = selectedAccount?.currency?.let {
        "(${it.name})"
    } ?: ""

    return "${wallet.name} $currencyText".trim()
}
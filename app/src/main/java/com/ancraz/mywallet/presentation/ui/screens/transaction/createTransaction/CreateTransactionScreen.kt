package com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.models.WalletType
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
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components.CategoryListMenu
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components.WalletListMenu
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryContainerColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun CreateTransactionScreen(
    transactionType: TransactionType,
    paddingValues: PaddingValues,
    viewModel: CreateTransactionViewModel = hiltViewModel<CreateTransactionViewModel>(),
    onEvent: (CreateTransactionUiEvent) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }

    CreateTransactionContainer(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        transactionType = transactionType,
        modifier = Modifier.padding(paddingValues),
        onEvent = { event ->
            when (event){
                is CreateTransactionUiEvent.AddTransaction -> {
                    viewModel.addNewTransaction(event.transaction)
                    onEvent(
                        CreateTransactionUiEvent.GoBack
                    )
                } else -> {
                    onEvent(event)
                }
            }
        }
    )
}


@Composable
private fun CreateTransactionContainer(
    uiState: CreateTransactionUiState,
    transactionType: TransactionType,
    modifier: Modifier = Modifier,
    onEvent: (CreateTransactionUiEvent) -> Unit
) {

    val context = LocalContext.current

    val inputValueState = rememberSaveable { mutableStateOf(0f.toFormattedString()) }
    val descriptionState = rememberSaveable { mutableStateOf<String?>(null) }

    val isCategoryListOpen = rememberSaveable { mutableStateOf(false) }
    val isWalletListOpen = rememberSaveable { mutableStateOf(false) }
    val isWalletAccountsDialogOpen = rememberSaveable { mutableStateOf(false) }

    val currencyState = rememberSaveable { mutableStateOf(CurrencyCode.USD) }
    val selectedWallet = rememberSaveable {
        mutableStateOf<WalletUi?>(null)
    }

    val selectedWalletCurrencyAccount = rememberSaveable {
        mutableStateOf<WalletUi.CurrencyAccountUi?>(null)
    }

    LaunchedEffect(uiState.data) {
        currencyState.value = uiState.data.recentCurrency

        selectedWallet.value = uiState.data.recentWalletId?.let { walletId ->
            uiState.data.walletList.find { wallet ->
                wallet.id == walletId
            }
        }
    }

    LaunchedEffect(selectedWallet.value) {
        selectedWalletCurrencyAccount.value = selectedWallet.value?.accounts?.find { account ->
            account.currency == currencyState.value
        } ?: selectedWallet.value?.accounts?.getOrNull(0)
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
                onEvent(CreateTransactionUiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (isWalletAccountsDialogOpen.value) {
            selectedWallet.value?.let { selectedWallet ->
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
                title = "Total balance: ${uiState.data.totalBalance}",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )

            if (!uiState.error.isNullOrEmpty()){
                ErrorText()
            } else {
                WalletSelectionButton(
                    selectedWallet = selectedWallet.value,
                    selectedWalletCurrencyAccount = selectedWalletCurrencyAccount.value,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClicked = {
                        isWalletListOpen.value = true
                    }
                )

                HorizontalSpacer()

                if (currencyState.value != CurrencyCode.USD) {
                    RateInfoText(
                        currentCurrencyState = currencyState.value,
                        rates = uiState.data.currencyRates
                    )
                    HorizontalSpacer()
                }
            }
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
                        title = stringResource(R.string.create_transaction_select_category_button),
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
                        if (!uiState.error.isNullOrEmpty()){
                            showToast(context, "Fix the error to add a new transaction")
                        } else {
                            buildTransactionObject(
                                value = inputValueState.value.toFloatValue(),
                                currency = currencyState.value,
                                type = transactionType,
                                description = descriptionState.value ?: category.name,
                                category = category,
                                wallet = selectedWallet.value,
                                selectedAccount = selectedWalletCurrencyAccount.value,
                                onSuccess = { transactionObject ->
                                    onEvent(
                                        CreateTransactionUiEvent.AddTransaction(transactionObject)
                                    )
                                },
                                onError = { message ->
                                    showToast(
                                        context, message
                                    )
                                }
                            )
                        }
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
                        selectedWallet.value = wallet
                        isWalletAccountsDialogOpen.value = true

                    },
                    onClose = {
                        isWalletListOpen.value = false
                    },
                    onEvent = { event ->
                        onEvent(event)
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
    if (rates.isEmpty()) {
        return
    }

    val baseText = stringResource(R.string.create_transaction_currency_rate_base_text)
    val currentRateIndex = rates.map { rate -> rate.currencyCode }.indexOf(currentCurrencyState)
    val rateText = "${rates[currentRateIndex].rate} ${currentCurrencyState.name}"
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$baseText $rateText",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = onSurfaceColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WalletSelectionButton(
    selectedWallet: WalletUi?,
    selectedWalletCurrencyAccount: WalletUi.CurrencyAccountUi?,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = primaryColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClicked()
            }
    ) {
        Text(
            text = (
                    getSelectedWalletInfoString(
                        selectedWallet,
                        selectedWalletCurrencyAccount
                    ) ?: stringResource(R.string.edit_transaction_no_wallet_title)
                    ).uppercase(),
            color = primaryColor,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}


@Composable
private fun TransactionDescriptionTextField(
    textState: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(fontSize = 16.sp)
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = textState.value ?: "",
        onValueChange = {
            textState.value = it
        },
        singleLine = true,
        textStyle = textStyle,
        placeholder = {
            Text(
                text = stringResource(R.string.create_transaction_description_placeholder),
                fontSize = 16.sp
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
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
                    text = stringResource(R.string.create_transaction_select_currency_account_dialog_title),
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
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable{
                                    selectedAccountState.value = account
                                }
                        ) {
                            RadioButton(
                                selected = account == selectedAccountState.value,
                                onClick = {
                                    selectedAccountState.value = account
                                }
                            )

                            VerticalSpacer()

                            Text(
                                text = "${account.moneyValue} ${account.currency}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = onBackgroundColor,
                                modifier = Modifier
                                    .padding(end = 10.dp)
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
                            text = stringResource(R.string.edit_transaction_cancel_button),
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
                            text = stringResource(R.string.edit_transaction_save_button),
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

@Composable
private fun ErrorText(
    modifier: Modifier = Modifier
){
    Text(
        text = stringResource(R.string.currency_rates_loading_error_message),
        color = MaterialTheme.colorScheme.error,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}


@Preview
@Composable
private fun TransactionInputScreenPreview() {
    MyWalletTheme {
        CreateTransactionContainer(
            uiState = CreateTransactionUiState(
                error = "Can not load currency rates.",
                data = CreateTransactionUiState.TransactionScreenData(
                    totalBalance = "5000.00",
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
    if (wallet == null){
        onError("Need to select a wallet")
    }
    selectedAccount?.let { account ->
        if (account.moneyValue.toFloatValue() < value && type == TransactionType.EXPENSE) {
            onError("You are in a minus. Not enough money on selected account")
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
): String? {
    if (wallet == null)
        return null

    val currencyText = selectedAccount?.currency?.let {
        "(${it.name})"
    } ?: ""

    return "${wallet.name} $currencyText".trim()
}


private fun showToast(
    context: Context,
    message: String
){
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    )
        .show()
}
package com.ancraz.mywallet.presentation.ui.screens.wallet.createWallet

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.CreateWalletUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun CreateWalletScreen(
    uiState: WalletUiState,
    modifier: Modifier,
    onEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val isWalletEdit = remember { mutableStateOf(uiState.wallet != null) }

    val nameState = remember { mutableStateOf(uiState.wallet?.name) }
    val descriptionState = remember { mutableStateOf(uiState.wallet?.description) }
    val selectedType = remember { mutableStateOf(uiState.wallet?.walletType) }
    val currencyList =
        remember {
            mutableStateOf(
                uiState.wallet?.accounts ?: listOf(
                    WalletUi.CurrencyAccountUi(
                        currency = CurrencyCode.USD,
                        moneyValue = 0f
                    )
                )
            )
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenHorizontalPadding)
    ) {
        HorizontalSpacer()

        NavigationToolbar(
            title = if (isWalletEdit.value) "Edit Wallet" else "New Wallet",
            onClickBack = {
                onEvent(UiEvent.GoBack)
            }
        )

        HorizontalSpacer(height = 20.dp)

        TitleText(
            text = "Name"
        )

        HorizontalSpacer()

        InputTextField(
            textState = nameState,
            placeholderText = "Wallet name",
            maxLines = 1
        )

        HorizontalSpacer()

        TitleText(
            text = "Description"
        )

        HorizontalSpacer()

        InputTextField(
            textState = descriptionState,
            placeholderText = "Wallet description",
            maxLines = 3
        )

        HorizontalSpacer()

        TitleText(
            text = "Type"
        )

        HorizontalSpacer()

        WalletTypeList(
            walletTypes = WalletType.entries,
            selectedType = selectedType
        )

        HorizontalSpacer()

        TitleText(
            text = "Currency Accounts"
        )

        CurrencyAccountList(
            accountList = currencyList,
            modifier = Modifier
                .weight(1f)
        )

        HorizontalSpacer()

        ActionButton (
            title = if (isWalletEdit.value) "Update Wallet" else "Add Wallet",
            onClick = {
                val wallet = buildWalletObject(
                    name = nameState.value,
                    description = descriptionState.value,
                    type = selectedType.value,
                    accounts = currencyList.value,
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                )

                wallet?.let {
                    if (isWalletEdit.value){
                        onEvent(CreateWalletUiEvent.UpdateWallet(it))
                    } else {
                        onEvent(CreateWalletUiEvent.AddWallet(it))
                    }

                    onEvent(UiEvent.GoBack)
                }
            }
        )

        HorizontalSpacer()
    }
}


@Composable
private fun TitleText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = onSecondaryColor,
        fontSize = 16.sp,
        modifier = modifier
    )
}


@Composable
private fun InputTextField(
    textState: MutableState<String?>,
    placeholderText: String,
    maxLines: Int,
    fontSize: TextUnit = 16.sp,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = textState.value ?: "",
        onValueChange = { text ->
            textState.value = text
        },
        maxLines = maxLines,
        textStyle = TextStyle(
            fontSize = fontSize,
            textDecoration = TextDecoration.None,
            color = onBackgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = fontSize
            )
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = secondaryColor,
            unfocusedContainerColor = secondaryColor,
            disabledContainerColor = secondaryColor,

            cursorColor = primaryColor,
            unfocusedPlaceholderColor = onBackgroundColor.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = primaryColor,
                shape = RoundedCornerShape(12.dp)
            )
    )
}


@Composable
private fun WalletTypeList(
    walletTypes: List<WalletType>,
    selectedType: MutableState<WalletType?>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
        verticalItemSpacing = 6.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(walletTypes) { walletType ->
            WalletTypeItem(
                type = walletType,
                selectedType = selectedType
            )
        }
    }
}


@Composable
private fun WalletTypeItem(
    type: WalletType,
    selectedType: MutableState<WalletType?>,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedType.value == type

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) primaryColor else backgroundColor
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(width = 1.dp, color = primaryColor),
        modifier = modifier,
        onClick = {
            selectedType.value = type
        }
    ) {
        Text(
            text = type.walletName,
            fontSize = 14.sp,
            maxLines = 1,
            color = if (isSelected) backgroundColor else onSurfaceColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
    }
}


@Composable
private fun CurrencyAccountList(
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
            value = account.moneyValue.toFormattedString(),
            onResult = { newValueString ->
                onEditAccount(
                    account.copy(
                        moneyValue = newValueString.toFloatValue()
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


@Composable
private fun AddAccountButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        val stroke = Stroke(
            width = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
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
                contentDescription = "Add Account",
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .size(24.dp)
            )

            VerticalSpacer()

            Text(
                text = "Add Account",
                color = onSecondaryColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun EditAccountValueDialog(
    value: String,
    modifier: Modifier = Modifier,
    onResult: (String) -> Unit,
    onCancel: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = value
            )
        )
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        textFieldValue.value = textFieldValue.value.copy(
            selection = TextRange(textFieldValue.value.text.length)
        )
    }

    Dialog(
        onDismissRequest = {
            onCancel()
        },
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
                    text = "Edit value",
                    color = onBackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(height = 20.dp)

                OutlinedTextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

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
                            onResult(textFieldValue.value.text)
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


@Composable
private fun SelectNewAccountCurrencyDialog(
    currencies: List<CurrencyCode>,
    modifier: Modifier = Modifier,
    onResult: (CurrencyCode) -> Unit,
    onCancel: () -> Unit
) {
    val selectedCurrency = remember { mutableStateOf(CurrencyCode.USD) }

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
                    text = "Edit value",
                    color = onBackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalSpacer(height = 20.dp)

                currencies.forEach { currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = currency == selectedCurrency.value,
                            onClick = {
                                selectedCurrency.value = currency
                            }
                        )

                        VerticalSpacer()

                        Text(
                            text = currency.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = onBackgroundColor
                        )
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
                            onResult(selectedCurrency.value)
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
private fun CreateWalletScreenPreview() {
    MyWalletTheme {
        CreateWalletScreen(
            uiState = WalletUiState(
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
            ),
            modifier = Modifier
                .background(backgroundColor),
            onEvent = {}
        )
    }
}


private fun buildWalletObject(
    name: String?,
    description: String?,
    type: WalletType?,
    accounts: List<WalletUi.CurrencyAccountUi>,
    onError: (String) -> Unit
): WalletUi? {
    if (name.isNullOrEmpty()) {
        onError("Wallet name cannot be empty")
        return null
    }
    if (type == null) {
        onError("Select wallet type")
        return null
    }
    return WalletUi(
        name = name,
        description = description,
        walletType = type,
        accounts = accounts,
        totalBalance = 0f
    )
}

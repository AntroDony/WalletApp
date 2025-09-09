package com.ancraz.mywallet.presentation.ui.screens.wallet.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.WalletType
import com.ancraz.mywallet.presentation.models.WalletUi
import com.ancraz.mywallet.presentation.ui.components.ActionButton
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.screens.wallet.WalletUiState
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.buildWalletObject
import com.ancraz.mywallet.presentation.ui.utils.getTestCurrencyAccountList

@Composable
fun WalletConfigContainer(
    uiState: WalletUiState,
    modifier: Modifier = Modifier,
    onActionButtonClicked: (WalletUi) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isWalletEditMode by rememberSaveable { mutableStateOf(uiState.wallet != null) }

    val nameState = rememberSaveable { mutableStateOf(uiState.wallet?.name) }
    val descriptionState = rememberSaveable { mutableStateOf(uiState.wallet?.description) }
    val selectedTypeState = rememberSaveable { mutableStateOf(uiState.wallet?.walletType) }
    val currencyListState =
        rememberSaveable {
            mutableStateOf(
                uiState.wallet?.accounts ?: listOf(
                    WalletUi.CurrencyAccountUi()
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
            title = if (isWalletEditMode) stringResource(R.string.edit_wallet_screen_title) else stringResource(
                R.string.create_wallet_screen_title
            ),
            onClickBack = {
                onBack()
            }
        )

        HorizontalSpacer(height = 20.dp)

        TitleText(
            text = stringResource(R.string.wallet_name_title)
        )

        HorizontalSpacer()

        InputTextField(
            textState = nameState,
            placeholderText = stringResource(R.string.wallet_name_placeholder_title),
            maxLines = 1,
            keyboardController = keyboardController
        )

        HorizontalSpacer()

        TitleText(
            text = stringResource(R.string.wallet_description_title)
        )

        HorizontalSpacer()

        InputTextField(
            textState = descriptionState,
            placeholderText = stringResource(R.string.wallet_description_placeholder_title),
            maxLines = 3,
            keyboardController = keyboardController
        )

        HorizontalSpacer()

        TitleText(
            text = stringResource(R.string.wallet_type_title)
        )

        HorizontalSpacer()

        WalletTypeList(
            walletTypes = WalletType.entries,
            selectedType = selectedTypeState
        )

        HorizontalSpacer()

        TitleText(
            text = stringResource(R.string.wallet_currency_accounts_title)
        )

        CurrencyAccountList(
            accountList = currencyListState,
            modifier = Modifier
                .weight(1f)
        )

        HorizontalSpacer()

        ActionButton(
            title = if (isWalletEditMode) {
                stringResource(R.string.update_wallet_button)
            } else {
                stringResource(R.string.create_wallet_button)
            },
            onClick = {
                val wallet = buildWalletObject(
                    id = uiState.wallet?.id,
                    name = nameState.value,
                    description = descriptionState.value,
                    type = selectedTypeState.value,
                    accounts = currencyListState.value,
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                )

                wallet?.let {
                    onActionButtonClicked(it)
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
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = textState.value ?: "",
        onValueChange = { text ->
            textState.value = text
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
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


@Preview
@Composable
private fun WalletConfigContainerPreview() {
    MyWalletTheme {
        WalletConfigContainer(
            uiState = WalletUiState(
                wallet = WalletUi(
                    name = "TBC Card",
                    description = "TBC Bank physic account",
                    walletType = WalletType.CARD,
                    accounts = getTestCurrencyAccountList(),
                    totalBalance = "2400 USD"
                ),
            ),
            modifier = Modifier
                .background(backgroundColor),
            onActionButtonClicked = {},
            onBack = {}
        )
    }
}
package com.ancraz.mywallet.presentation.ui.screens.inputScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.svg.SvgDecoder
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.states.TransactionCategoriesState
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.InputNumberKeyboard
import com.ancraz.mywallet.presentation.ui.components.InputTextField
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.SubmitButton
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryContainerColor
import com.ancraz.mywallet.presentation.ui.theme.surfaceColor
import com.ancraz.mywallet.presentation.ui.utils.toFloatValue
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun TransactionInputScreen(
    categoriesState: TransactionCategoriesState,
    totalBalance: Float = 0f,
    transactionType: TransactionType,
    modifier: Modifier = Modifier,
    onAddTransaction: (TransactionUi) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val inputValueState = remember { mutableStateOf(0f.toFormattedString()) }
    val currencyState = remember { mutableStateOf(CurrencyCode.USD) }

    val descriptionState = remember { mutableStateOf<String?>(null) }
    val isCategoryListOpen = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        HorizontalSpacer()

        NavigationToolbar(
            title = transactionType.name.lowercase().replaceFirstChar { it.uppercase() },
            onClickBack = onBack
        )

        HorizontalSpacer()

        InputTextField(
            valueState = inputValueState,
            currencyState = currencyState,
            title = "Balance: ${totalBalance.toFormattedString()}",
            modifier = Modifier
                .fillMaxWidth()
                //.weight(1f)
                .align(Alignment.CenterHorizontally)

        )

        HorizontalSpacer()

        if (!isCategoryListOpen.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                TransactionDescriptionTextField(descriptionState)

                HorizontalSpacer()

                InputNumberKeyboard(inputValueState)

                HorizontalSpacer()

                SubmitButton(
                    title = "Select category",
                    onClick = {
                        isCategoryListOpen.value = true
                    }
                )

                HorizontalSpacer()
            }
        } else {
            val categoryList = if (transactionType == TransactionType.INCOME) {
                categoriesState.incomeCategories
            } else {
                categoriesState.expenseCategories
            }

            CategoryListMenu(
                categoryList,
                openState = isCategoryListOpen,
                modifier = Modifier.weight(1f),
                onSelected = { category ->
                    isCategoryListOpen.value = false

                    val transactionObject = buildTransactionObject(
                        value = inputValueState.value.toFloatValue(),
                        currency = currencyState.value,
                        type = transactionType,
                        description = descriptionState.value ?: category.name,
                        category = category
                    )

                    if (transactionObject == null) {
                        Toast.makeText(context, "Transaction value cannot be 0", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        onAddTransaction(transactionObject)
                        onBack()
                    }
                }
            )
        }
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
private fun CategoryListMenu(
    categories: List<TransactionCategoryUi>,
    openState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onSelected: (TransactionCategoryUi) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                .background(surfaceColor)

        ){
            Image(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Close",
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
                    .clickable {
                        openState.value = false
                    }
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .background(surfaceColor),
            columns = GridCells.Fixed(3)) {
            items(categories) { category ->
                CategoryItem(
                    category,
                    modifier = Modifier
                        .clickable {
                            onSelected(category)
                        }
                )
            }
        }
    }
}


@Composable
private fun CategoryItem(
    category: TransactionCategoryUi,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val assetUri = "file:///android_asset/${category.iconAssetPath}"


    Card(
        modifier = modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        border = BorderStroke(width = 1.dp, color = primaryColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = assetUri,
                contentDescription = category.name,
                imageLoader = imageLoader,
                colorFilter = ColorFilter.tint(
                    Color.White
                ),
                modifier = Modifier
                    .padding(6.dp)
                    .size(40.dp)
            )

            Text(
                text = category.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1
            )

            HorizontalSpacer(modifier = Modifier.height(4.dp))
        }

    }
}


@Preview
@Composable
private fun TransactionInputScreenPreview() {
    MyWalletTheme {
        TransactionInputScreen(
            categoriesState = TransactionCategoriesState(
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
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                    TransactionCategoryUi(
                        name = "Category 1",
                        iconAssetPath = "categories_icon/house_category.svg",
                        transactionType = TransactionType.EXPENSE
                    ),
                )
            ),
            totalBalance = 8000f,
            TransactionType.EXPENSE,
            onAddTransaction = {},
            onBack = {}
        )
    }
}

private fun buildTransactionObject(
    value: Float,
    currency: CurrencyCode,
    type: TransactionType,
    description: String?,
    category: TransactionCategoryUi
): TransactionUi? {
    if (value == 0f) {
        return null
    }
    return TransactionUi(
        value = value,
        currency = currency,
        type = type,
        description = description,
        category = category
    )
}
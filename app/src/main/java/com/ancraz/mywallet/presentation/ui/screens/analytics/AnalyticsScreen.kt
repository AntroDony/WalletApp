package com.ancraz.mywallet.presentation.ui.screens.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.models.AnalyticsPeriod
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.models.TransactionUi
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.components.LoadingIndicator
import com.ancraz.mywallet.presentation.ui.components.NavigationToolbar
import com.ancraz.mywallet.presentation.ui.components.TransactionCard
import com.ancraz.mywallet.presentation.ui.components.TransactionTypeSelector
import com.ancraz.mywallet.presentation.ui.components.VerticalSpacer
import com.ancraz.mywallet.presentation.ui.events.AnalyticsUiEvent
import com.ancraz.mywallet.presentation.ui.events.UiEvent
import com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components.CategoryListMenu
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.errorColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSurfaceColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.screenHorizontalPadding
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.getFormattedPeriodLabel
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString
import java.util.Calendar

@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    modifier: Modifier,
    onEvent: (UiEvent) -> Unit
) {
    val selectedPeriodState = remember { mutableStateOf(uiState.data.period) }
    val selectedTypeState = remember { mutableStateOf<TransactionType?>(null) }

    val isCategoryListOpen = remember { mutableStateOf(false) }
    val categoryList = uiState.data.transactionCategoryList
    val selectedFilterCategoryState = remember { mutableStateOf<TransactionCategoryUi?>(null) }

    val datePageNumber = remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedPeriodState.value) {
        datePageNumber.value = 0
    }

    LaunchedEffect(
        selectedPeriodState.value,
        selectedTypeState.value,
        selectedFilterCategoryState.value,
        datePageNumber.value
    ) {
        onEvent(
            AnalyticsUiEvent.FilterAnalyticsData(
                type = selectedTypeState.value,
                category = selectedFilterCategoryState.value,
                period = selectedPeriodState.value,
                periodOffset = datePageNumber.value
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
            title = stringResource(R.string.analytics_screen_title),
            onClickBack = {
                onEvent(UiEvent.GoBack)
            }
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            if (isCategoryListOpen.value) {
                CategoryListMenu(
                    categories = categoryList,
                    onSelect = { category ->
                        selectedFilterCategoryState.value = category

                        isCategoryListOpen.value = false
                    },
                    onClose = {
                        isCategoryListOpen.value = false
                    }
                )
            }


            TotalBalanceView(
                uiState = uiState,
                period = selectedPeriodState.value,
                offset = datePageNumber.value
            )

            HorizontalSpacer(height = 30.dp)

            PeriodSelector(
                selectedPeriodState = selectedPeriodState,
            )

            HorizontalSpacer(height = 30.dp)

            AnalyticsDataView(
                uiState = uiState,
                pageState = datePageNumber,
                onPreviousPageClick = {
                    datePageNumber.value -= 1

                    debugLog("pageNumber: ${datePageNumber.value}")
                },
                onNextPageClick = {
                    datePageNumber.value += 1

                    debugLog("pageNumber: ${datePageNumber.value}")
                }
            )

            HorizontalSpacer()

            FilterAnalyticsView(
                filterCategoryState = selectedFilterCategoryState,
                onSelectCategory = {
                    isCategoryListOpen.value = true
                }
            )

            HorizontalSpacer()

            TransactionListView(
                transactionList = uiState.data.filteredTransactionList,
                onTransactionTypeSelected = { selectedTransactionType ->
                    selectedTypeState.value = selectedTransactionType
                },
                onClickTransaction = { transaction ->
                    onEvent(AnalyticsUiEvent.ShowTransactionInfo(transaction))
                }
            )
        }
    }
}


@Composable
private fun TotalBalanceView(
    uiState: AnalyticsUiState,
    period: AnalyticsPeriod,
    offset: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.analytics_balance_title) + " ${
                getFormattedPeriodLabel(
                    period, offset
                )
            }",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = onBackgroundColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        HorizontalSpacer()

        if (uiState.isLoading) {
            CircularProgressIndicator(
                trackColor = onPrimaryColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                text = "\$ ${uiState.data.totalBalanceInUsd.toFormattedString()}",
                color = onBackgroundColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
private fun PeriodSelector(
    selectedPeriodState: MutableState<AnalyticsPeriod>,
    modifier: Modifier = Modifier,
    //onPeriodSelected: (AnalyticsPeriod) -> Unit
) {

    val periodList = listOf(
        AnalyticsPeriod.Day,
        AnalyticsPeriod.Week,
        AnalyticsPeriod.Month,
        AnalyticsPeriod.Year
    )

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(secondaryColor)
    ) {
        periodList.forEach { period ->
            PeriodItem(
                title = period.name,
                isSelected = period == selectedPeriodState.value,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedPeriodState.value = period
//                    onPeriodSelected(
//                        period
//                    )
                }
            )
        }
    }
}


@Composable
private fun PeriodItem(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) primaryColor else secondaryColor
            )
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) onPrimaryColor else onSecondaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}


@Composable
private fun AnalyticsDataView(
    uiState: AnalyticsUiState,
    pageState: MutableState<Int>,
    modifier: Modifier = Modifier,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit
) {
    val isNextPageButtonActive = pageState.value < 0

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
            contentDescription = "Previous page",
            colorFilter = ColorFilter.tint(color = primaryColor),
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable {
                    onPreviousPageClick()
                },
        )

        AnalyticsView(
            title = stringResource(R.string.analytics_income_title),
            value = uiState.data.incomeValueInUsd,
            icon = Icons.AutoMirrored.Outlined.TrendingUp,
            contentColor = primaryColor,
            modifier = Modifier.weight(1f)
        )

        AnalyticsView(
            title = stringResource(R.string.analytics_expense_title),
            value = uiState.data.expenseValueInUsd,
            icon = Icons.AutoMirrored.Outlined.TrendingDown,
            contentColor = errorColor,
            modifier = Modifier.weight(1f)
        )


        Image(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = "Previous page",
            colorFilter = if (isNextPageButtonActive) {
                ColorFilter.tint(color = primaryColor)
            } else {
                ColorFilter.tint(color = secondaryColor)
            },
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable {
                    if (isNextPageButtonActive) {
                        debugLog("nextPage click")
                        onNextPageClick()
                    }
                }
        )
    }
}


@Composable
private fun AnalyticsView(
    title: String,
    value: Float,
    icon: ImageVector,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = icon,
            contentDescription = title,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = Modifier
                .size(50.dp)
        )

        HorizontalSpacer(height = 8.dp)

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = onBackgroundColor
        )

        HorizontalSpacer(height = 8.dp)

        Text(
            text = "\$ ${value.toFormattedString()}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Composable
private fun FilterAnalyticsView(
    filterCategoryState: MutableState<TransactionCategoryUi?>,
    modifier: Modifier = Modifier,
    onSelectCategory: () -> Unit
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(width = 1.dp, color = primaryColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter_title),
                color = onBackgroundColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            VerticalSpacer(width = 20.dp)

            Text(
                text = filterCategoryState.value?.name ?:stringResource(R.string.filter_no_title),
                color = onBackgroundColor,
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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
                        .clickable{
                            onSelectCategory()
                        }
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
                            filterCategoryState.value = null
                        }
                )
            }
        }
    }
}


@Composable
private fun TransactionListView(
    transactionList: List<TransactionUi>,
    modifier: Modifier = Modifier,
    onTransactionTypeSelected: (TransactionType?) -> Unit,
    onClickTransaction: (TransactionUi) -> Unit
) {
    TransactionTypeSelector(
        onTypeSelected = { type ->
            onTransactionTypeSelected(
                type
            )
        }
    )

    HorizontalSpacer()

    if (transactionList.isEmpty()) {
        Box(
            modifier = modifier
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
                    text = stringResource(R.string.analytics_empty_transaction_list_placeholder),
                    color = onSurfaceColor,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }

        }
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(transactionList) { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onClick = {
                        onClickTransaction(transaction)
                    }
                )
            }
        }
    }

}


@Preview
@Composable
private fun AnalyticsScreenPreview() {
    MyWalletTheme {
        AnalyticsScreen(
            uiState = AnalyticsUiState(
                isLoading = false,
                data = AnalyticsUiState.AnalyticsScreenData(
                    totalBalanceInUsd = 10000f,
                    incomeValueInUsd = 2500f,
                    expenseValueInUsd = 300f,
                    filteredTransactionList = listOf(
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
                        )
                    )
                )
            ),
            modifier = Modifier.background(backgroundColor),
            onEvent = {}
        )
    }
}
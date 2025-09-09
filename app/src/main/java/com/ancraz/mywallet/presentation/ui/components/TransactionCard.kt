package com.ancraz.mywallet.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.errorColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.utils.timeToString
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun TransactionCard(
    transaction: TransactionUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    val assetUri = "file:///android_asset/${transaction.category?.iconAssetPath}"

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
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = assetUri,
                contentDescription = transaction.category?.name,
                imageLoader = imageLoader,
                colorFilter = ColorFilter.tint(primaryColor),
                modifier = Modifier
                    .size(40.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 300.dp)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = transaction.category?.name ?: "",
                    fontSize = 16.sp,
                    color = onBackgroundColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                HorizontalSpacer(modifier = Modifier.height(4.dp))

                Text(
                    text = transaction.time.timeToString(),
                    fontSize = 14.sp,
                    color = onBackgroundColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TransactionValueText(
                transaction,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}



@Composable
private fun TransactionValueText(
    transaction: TransactionUi,
    modifier: Modifier = Modifier
){
    val textColor = if (transaction.type == TransactionType.INCOME) primaryColor else errorColor
    val valuePrefix = if (transaction.type == TransactionType.INCOME){
        "+"
    } else if (transaction.type == TransactionType.EXPENSE) {
        "-"
    } else {
        ""
    }

    val valueSuffix = transaction.currency.name

    Text(
        text = "$valuePrefix${transaction.value.toFormattedString()} $valueSuffix",
        fontSize = 16.sp,
        textAlign = TextAlign.End,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}


@Preview
@Composable
fun TransactionCardPreview(){
    MyWalletTheme {
        TransactionCard(
            transaction = TransactionUi(
                value = 2000f,
                currency = CurrencyCode.USD,
                type = TransactionType.INCOME,
                category = TransactionCategoryUi(
                    name = "Food",
                    transactionType = TransactionType.INCOME,
                    iconAssetPath = "categories_icon/products_category.svg"
                )
            ),
            onClick = {}
        )
    }
}
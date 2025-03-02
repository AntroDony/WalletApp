package com.ancraz.mywallet.presentation.ui.screens.transaction.createTransaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.svg.SvgDecoder
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.models.TransactionCategoryUi
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.surfaceColor



@Composable
fun CategoryListMenu(
    categories: List<TransactionCategoryUi>,
    modifier: Modifier = Modifier,
    onSelect: (TransactionCategoryUi) -> Unit,
    onClose: () -> Unit
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

        ) {
            Image(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Close",
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
                    .clickable {
                        onClose()
                    }
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .background(surfaceColor),
            columns = GridCells.Fixed(3)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category,
                    modifier = Modifier
                        .clickable {
                            onSelect(category)
                            onClose()
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
private fun CategoryListMenuPreview(){
    MyWalletTheme {
        CategoryListMenu (
            categories = listOf(
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
                )
            ),
            onSelect = {},
            onClose = {}
        )
    }
}
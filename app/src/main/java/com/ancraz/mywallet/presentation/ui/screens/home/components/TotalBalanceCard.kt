package com.ancraz.mywallet.presentation.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.presentation.ui.components.HorizontalSpacer
import com.ancraz.mywallet.presentation.ui.events.HomeUiEvent
import com.ancraz.mywallet.presentation.ui.screens.home.HomeUiState
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.onSecondaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.theme.secondaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

@Composable
fun TotalBalanceCard(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {
    val isPrivateMode = state.data.isPrivateMode

    Card(
        modifier = modifier
            .padding(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.home_total_balance_card_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = onPrimaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Image(
                    imageVector = if (isPrivateMode) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(onPrimaryColor),
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            onEvent(
                                HomeUiEvent.ChangePrivateMode(!isPrivateMode)
                            )
                        }
                )
            }

            HorizontalSpacer()

            if (state.isLoading) {
                CircularProgressIndicator(
                    trackColor = onPrimaryColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = if (isPrivateMode) stringResource(R.string.home_card_private_mode_balance) else "\$ ${state.data.balance.toFormattedString()}",
                    color = onPrimaryColor,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TotalBalanceActionButton(
                    text = stringResource(R.string.home_card_income_button),
                    icon = Icons.Filled.Add
                ) {
                    onEvent(HomeUiEvent.CreateTransaction(TransactionType.INCOME))
                }

                TotalBalanceActionButton(
                    text = stringResource(R.string.home_card_expense_button),
                    icon = Icons.Filled.Remove
                ) {
                    onEvent(HomeUiEvent.CreateTransaction(TransactionType.EXPENSE))
                }

                TotalBalanceActionButton(
                    text = stringResource(R.string.home_card_edit_button),
                    icon = Icons.Filled.Edit
                ) {
                    onEvent(HomeUiEvent.EditTotalBalance(state.data.balance))
                }

                TotalBalanceActionButton(
                    text = stringResource(R.string.home_card_analytics_button),
                    icon = Icons.AutoMirrored.Outlined.TrendingUp
                ) {
                    onEvent(HomeUiEvent.ShowAnalytics)
                }
            }

        }
    }
}



@Composable
private fun TotalBalanceActionButton(
    text: String,
    icon: ImageVector,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(secondaryColor)
                .clickable {
                    onAction()
                }
        ) {
            Image(
                imageVector = icon,
                contentDescription = text,
                colorFilter = ColorFilter.tint(onSecondaryColor),
                modifier = Modifier
                    .padding(12.dp)
                    .size(26.dp)
            )
        }

        Text(
            text = text,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Preview
@Composable
private fun TotalBalanceCardPreview(){
    MyWalletTheme {
        TotalBalanceCard(
            state = HomeUiState(
                isLoading = false,
                data = HomeUiState.HomeScreenData(
                    balance = 8000f,
                    isPrivateMode = true
                ),
            ),
            onEvent = {}
        )
    }
}
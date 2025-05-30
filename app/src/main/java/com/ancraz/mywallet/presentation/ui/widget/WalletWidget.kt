package com.ancraz.mywallet.presentation.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.background
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider
import com.ancraz.mywallet.R
import com.ancraz.mywallet.core.utils.Constants
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.presentation.ui.MainActivity
import com.ancraz.mywallet.presentation.ui.theme.MyWalletTheme
import com.ancraz.mywallet.presentation.ui.theme.backgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onBackgroundColor
import com.ancraz.mywallet.presentation.ui.theme.onPrimaryColor
import com.ancraz.mywallet.presentation.ui.theme.primaryColor
import com.ancraz.mywallet.presentation.ui.utils.toFormattedString

object WalletWidget: GlanceAppWidget() {

    val totalBalanceKey = floatPreferencesKey(Constants.Prefs.TOTAL_BALANCE_KEY)
    val isPrivateModeKey = booleanPreferencesKey(Constants.Prefs.PRIVATE_MODE_KEY)

    private val destinationKey = ActionParameters.Key<String>(
        Constants.Widget.START_SCREEN_PATH_KEY
    )

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode
        get() = SizeMode.Single


    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        debugLog("glanceError: ${throwable.message}")
    }


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }


    @Composable
    private fun WidgetContent(){
        val prefs = currentState<Preferences>()

        val totalBalance = prefs[totalBalanceKey] ?: 0f
        val isPrivateMode = prefs[isPrivateModeKey] ?: false

        Scaffold (
            backgroundColor = ColorProvider(backgroundColor),
            modifier = GlanceModifier
                .fillMaxSize()
                .cornerRadius(15.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .cornerRadius(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = GlanceModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (isPrivateMode) "* * * *" else "\$ ${totalBalance.toFormattedString()}",
                        maxLines = 1,
                        style = TextDefaults.defaultTextStyle.copy(
                            color = ColorProvider(onBackgroundColor),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = GlanceModifier
                    )
                }


                Spacer(modifier = GlanceModifier
                    .width(40.dp)
                )

                Row(
                    modifier = GlanceModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        imageProvider = ImageProvider(R.drawable.ic_widget_add),
                        contentDescription = "Income",
                        backgroundColor = ColorProvider(
                            color = primaryColor
                        ),
                        contentColor = ColorProvider(
                            onPrimaryColor
                        ),
                        modifier = GlanceModifier
                            .size(34.dp),
                        onClick = actionStartActivity<MainActivity>(
                            actionParametersOf(destinationKey to Constants.Widget.INCOME_SCREEN_PATH_VALUE)
                        )
                    )

                    Spacer(modifier = GlanceModifier
                        .width(20.dp)
                    )

                    CircleIconButton(
                        imageProvider = ImageProvider(R.drawable.ic_widget_remove),
                        contentDescription = "Expense",
                        backgroundColor = ColorProvider(
                            color = primaryColor
                        ),
                        contentColor = ColorProvider(
                            onPrimaryColor
                        ),
                        modifier = GlanceModifier
                            .size(34.dp),
                        onClick = actionStartActivity<MainActivity>(
                            actionParametersOf(destinationKey to Constants.Widget.EXPENSE_SCREEN_PATH_VALUE)
                        )
                    )

                    Spacer(modifier = GlanceModifier
                        .width(14.dp)
                        .defaultWeight()
                    )
                }
            }
        }
    }


}
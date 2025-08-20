package com.ancraz.mywallet.presentation.models

import android.os.Parcelable
import com.ancraz.mywallet.core.models.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionCategoryUi(
    val id: Long = 0L,
    val name: String,
    val iconAssetPath: String,
    val transactionType: TransactionType
): Parcelable
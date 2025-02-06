package com.ancraz.mywallet.domain.models

import com.ancraz.mywallet.core.models.CurrencyCode
import com.ancraz.mywallet.core.models.TransactionType

data class Transaction(

    val id: Long,

    val time: Long,

    val value: Float,

    val currencyCode: CurrencyCode,

    val transactionType: TransactionType,

    val description: String?,

    val category: TransactionCategory?

)
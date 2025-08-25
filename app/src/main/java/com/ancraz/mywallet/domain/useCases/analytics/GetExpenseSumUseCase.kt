package com.ancraz.mywallet.domain.useCases.analytics

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.converter.CurrencyConverter
import com.ancraz.mywallet.domain.models.Transaction
import javax.inject.Inject

class GetExpenseSumUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    private val currencyConverter = CurrencyConverter(dataStoreRepository)

    suspend operator fun invoke(transactions: List<Transaction>): Float {
        return transactions
            .filter { it.transactionType == TransactionType.EXPENSE }
            .map { transaction ->
                currencyConverter.convertToUsd(transaction.value, transaction.currencyCode) ?: 0f
            }.sum()
    }
}
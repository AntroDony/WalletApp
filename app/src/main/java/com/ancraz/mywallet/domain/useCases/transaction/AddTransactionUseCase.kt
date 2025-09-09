package com.ancraz.mywallet.domain.useCases.transaction

import com.ancraz.mywallet.domain.converter.CurrencyConverter
import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.data.storage.dataStore.DataStoreRepository
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.repository.TransactionRepository
import com.ancraz.mywallet.domain.repository.WalletRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    private val currencyConverter = CurrencyConverter(dataStoreRepository)

    suspend operator fun invoke(transaction: Transaction){
        when(transaction.transactionType){
            TransactionType.INCOME-> {
                addIncomeTransaction(transaction)
            }

            TransactionType.EXPENSE -> {
                addExpenseTransaction(transaction)
            }
        }
    }

    private suspend fun addIncomeTransaction(transaction: Transaction){
        transactionRepository.addNewTransaction(transaction)

        dataStoreRepository.incomeTotalBalance(
            currencyConverter.convertToUsd(
                value = transaction.value,
                currencyCode = transaction.currencyCode
            ) ?: 0f
        )

        transaction.updateWalletBalance(
            TransactionType.INCOME
        )
    }


    private suspend fun addExpenseTransaction(transaction: Transaction){
        transactionRepository.addNewTransaction(transaction)

        dataStoreRepository.expenseTotalBalance(
            currencyConverter.convertToUsd(
                value = transaction.value,
                currencyCode = transaction.currencyCode
            ) ?: 0f
        )

        transaction.updateWalletBalance(
            TransactionType.EXPENSE
        )
    }


    private suspend fun Transaction.updateWalletBalance(transactionType: TransactionType){
        if (this.wallet != null && this.selectedWalletAccount != null){
            when(transactionType){
                TransactionType.INCOME -> {
                    walletRepository.incomeToWallet(
                        wallet = this.wallet,
                        selectedAccount = this.selectedWalletAccount,
                        value = this.value
                    )
                }

                TransactionType.EXPENSE -> {
                    walletRepository.expenseFromWallet(
                        wallet = this.wallet,
                        selectedAccount = this.selectedWalletAccount,
                        value = this.value
                    )
                }
            }
        }
    }
}
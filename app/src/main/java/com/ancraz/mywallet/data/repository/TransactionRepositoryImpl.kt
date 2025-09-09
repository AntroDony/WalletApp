package com.ancraz.mywallet.data.repository

import com.ancraz.mywallet.core.models.TransactionType
import com.ancraz.mywallet.core.utils.debugLog
import com.ancraz.mywallet.data.mappers.toCategoryEntity
import com.ancraz.mywallet.data.mappers.toTransaction
import com.ancraz.mywallet.data.mappers.toTransactionCategory
import com.ancraz.mywallet.data.mappers.toTransactionEntity
import com.ancraz.mywallet.data.mappers.toWallet
import com.ancraz.mywallet.data.mappers.toWalletEntity
import com.ancraz.mywallet.data.storage.database.dao.CategoryDao
import com.ancraz.mywallet.data.storage.database.dao.TransactionDao
import com.ancraz.mywallet.data.storage.database.dao.WalletDao
import com.ancraz.mywallet.domain.models.TransactionCategory
import com.ancraz.mywallet.domain.models.Transaction
import com.ancraz.mywallet.domain.models.Wallet
import com.ancraz.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val walletDao: WalletDao
) : TransactionRepository {

    override fun getTransactionList(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { list ->
            list.map { transaction ->
                val wallet = getWalletById(transaction.walletId)
                transaction.toTransaction(wallet)
            }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction {
        val transaction = transactionDao.getTransactionById(id)
        val wallet = getWalletById(transaction.walletId)
        return transaction.toTransaction(wallet)
    }

    override suspend fun addNewTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toTransactionEntity())

        val wallet = getWalletById(transaction.wallet?.id)
        wallet?.let {
            updateWalletAfterTransactionWasDeleted(
                it,
                transaction
            )
        }
    }

    override suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }


    override fun getExpenseCategoryList(): Flow<List<TransactionCategory>> {
        return categoryDao.getAllExpenseCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override fun getIncomeCategoryList(): Flow<List<TransactionCategory>> {
        return categoryDao.getAllIncomeCategories().map { list ->
            list.map { category ->
                category.toTransactionCategory()
            }
        }
    }


    override suspend fun addNewExpenseCategory(category: TransactionCategory) {
        categoryDao.insertCategory(category.toCategoryEntity())
    }


    override suspend fun addNewIncomeCategory(category: TransactionCategory) {
        categoryDao.insertCategory(category.toCategoryEntity())
    }


    override suspend fun deleteCategoryById(id: Long) {
        categoryDao.deleteCategoryById(id)
    }


    private suspend fun getWalletById(walletId: Long?): Wallet? {
        if (walletId == null) {
            return null
        }

        return try {
            walletDao.getWalletById(walletId).toWallet()
        } catch (e: IllegalStateException) {
            debugLog("cannot get wallet by id: ${e.message}")
            null
        } catch (e: Exception) {
            debugLog("cannot get wallet by id: ${e.message}")
            null
        }
    }

    private suspend fun updateWalletAfterTransactionWasDeleted(
        wallet: Wallet,
        transaction: Transaction
    ) {
        if (!wallet.currencyAccountList.map { it.currencyCode }
                .contains(transaction.currencyCode)
        ) {
            debugLog("cannot updateWallet: wallet account is not found")
            return
        }

        run {
            wallet.currencyAccountList.forEach { updatedCurrencyAccount ->
                if (updatedCurrencyAccount.currencyCode == transaction.currencyCode){
                    if (transaction.transactionType == TransactionType.INCOME){
                        updatedCurrencyAccount.value -= transaction.value
                    } else if (transaction.transactionType == TransactionType.EXPENSE){
                        updatedCurrencyAccount.value += transaction.value
                    }
                    return@run
                }
            }
        }
        walletDao.updateWallet(wallet.toWalletEntity())
    }

}
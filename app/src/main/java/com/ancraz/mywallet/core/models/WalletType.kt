package com.ancraz.mywallet.core.models

enum class WalletType(val walletName: String) {
    CASH("Cash"),
    CARD("Card"),
    BANK_ACCOUNT("Bank Account"),
    CRYPTO_WALLET("Crypto Wallet"),
    INVESTMENTS("Investments"),
    OTHER("Other")
}
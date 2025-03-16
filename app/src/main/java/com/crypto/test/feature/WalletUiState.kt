package com.crypto.test.feature

data class WalletUiState(
    val symbol: String = "$",
    val balance: String = "0",
    val currency: String = "USD",
    val currenciesUiState: List<WalletCurrencyUiState> = emptyList()
) {
    companion object {
        val Uninitialized = WalletUiState()
    }
}

data class WalletCurrencyUiState(
    val iconUrl: String,
    val name: String,
    val currencyBalance: String,
    val currency: String,
    val anchorSymbol: String = "$",
    val anchorCurrencyBalance: String = "0",
    val anchorCurrency: String = "USD",
)

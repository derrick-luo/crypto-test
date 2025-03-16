package com.crypto.test.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.test.data.UserRepository
import com.crypto.test.data.WalletRepository
import com.crypto.test.data.dto.Balance
import com.crypto.test.data.dto.Currency
import com.crypto.test.data.dto.Tier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.math.BigDecimal

class WalletViewModel(
    private val userRepository: UserRepository,
    private val walletRepo: WalletRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<WalletUiState> =
        MutableStateFlow(WalletUiState.Uninitialized)
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchBalance()
        }
    }

    suspend fun fetchBalance() {

        val token = userRepository.getCurrentUserToken()
        if (token.isBlank()) {
            _uiState.value = WalletUiState.NotLoggedIn
            return
        }

        return walletRepo.getBalance(token)
            .zip(walletRepo.getRates()) { balances, tiers ->
                balances.calculateValue(tiers)
            }
            .zip(walletRepo.getCurrencies()) { walletCurrencyUiStates, currencies ->
                walletCurrencyUiStates.setCurrencyData(currencies)
            }
            .collect { walletCurrencyUiStates ->

                val balance = walletCurrencyUiStates.sumOf { walletCurrencyUiState ->
                    BigDecimal(walletCurrencyUiState.anchorCurrencyBalance)
                }

                _uiState.value = WalletUiState(
                    balance = balance.toString(),
                    currenciesUiState = walletCurrencyUiStates
                )
            }
    }

    /**
     * calculate value of each currencies in balance base on provided [tiers]
     */
    private fun List<Balance>.calculateValue(
        tiers: List<Tier>,
        anchorSymbol: String = "$",
        anchorCurrency: String = "USD"
    ): List<WalletCurrencyUiState> {

        return this.map { balance ->

            val rate = tiers.find {
                it.fromCurrency == balance.currency
                        && it.toCurrency == anchorCurrency
            }?.rates?.first()?.rate

            val anchorCurrencyBalance = runCatching {
                balance.amount.times(BigDecimal(rate)).toString()
            }.getOrElse { "unknown" }

            WalletCurrencyUiState(
                currencyBalance = balance.amount.toString(),
                currency = balance.currency,
                anchorSymbol = anchorSymbol,
                anchorCurrencyBalance = anchorCurrencyBalance,
                anchorCurrency = anchorCurrency
            )
        }
    }

    private fun List<WalletCurrencyUiState>.setCurrencyData(
        currencies: List<Currency>
    ): List<WalletCurrencyUiState> {
        return this.map { walletCurrencyUiState ->

            val currency = currencies.find {
                it.id == walletCurrencyUiState.currency
            }

            walletCurrencyUiState.copy(
                iconUrl = currency?.iconUrl ?: "",
                // use id as displayed name if we can't find currency data
                name = currency?.name?: walletCurrencyUiState.currency
            )
        }
    }


    companion object {
        private val TAG = WalletViewModel::class.simpleName
    }
}

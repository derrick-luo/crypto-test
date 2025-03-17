package com.crypto.test.data

import com.crypto.test.data.dto.Balance
import com.crypto.test.data.dto.Currency
import com.crypto.test.data.dto.Rate
import com.crypto.test.data.dto.Tier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal

/**
 * mocked remote wallet data source
 *
 * for simplicity, json data deserialization and network requests implementation are omitted.
 */
class RemoteWalletDataSource : WalletDataSource {

    override suspend fun getRates(): Flow<List<Tier>> {
        // mock a get rates network request
        return flow {
            delay(500)
            emit(rates)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCurrencies(): Flow<List<Currency>> {
        // mock a get currencies network request
        return flow {
            delay(500)
            emit(currencies)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBalance(userToken: String): Flow<List<Balance>> {
        // mock a get balance network request
        return flow {
            delay(500)
            emit(balance)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * mocked responses data
     * only BTC, ETH and CRO related data are added according to the requirement.
     */
    companion object {

        private val rates = listOf(
            Tier(
                fromCurrency = "BTC",
                toCurrency = "USD",
                rates = listOf(
                    Rate(
                        amount = "1000",
                        rate = "10603.900000",
                    ),
                ),
                timestamp = 1602080062
            ),
            Tier(
                fromCurrency = "ETH",
                toCurrency = "USD",
                rates = listOf(
                    Rate(
                        amount = "1000",
                        rate = "340.210000",
                    ),
                ),
                timestamp = 1602080062
            ),
            Tier(
                fromCurrency = "CRO",
                toCurrency = "USD",
                rates = listOf(
                    Rate(
                        amount = "1000",
                        rate = "0.147268",
                    ),
                ),
                timestamp = 1602080062
            ),
        )

        private val currencies = listOf(
            Currency(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c1246f55568a400e48ac233/bitcoin.png",
            ),
            Currency(
                id = "ETH",
                name = "Ethereum",
                symbol = "ETH",
                iconUrl = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c12487d5568a4017c20a993/ethereum.png",
            ),
            Currency(
                id = "CRO",
                name = "Crypto.com Coin",
                symbol = "CRO",
                iconUrl = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c1248c15568a4017c20aa87/cro.png",
            ),
        )

        private val balance = listOf(
            Balance(
                currency = "BTC",
                amount = BigDecimal.valueOf(1.4)
            ),
            Balance(
                currency = "ETH",
                amount = BigDecimal.valueOf(20.3)
            ),
            Balance(
                currency = "CRO",
                amount = BigDecimal.valueOf(259.1)
            ),
        )
    }
}

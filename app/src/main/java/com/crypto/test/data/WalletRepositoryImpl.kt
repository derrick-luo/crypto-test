package com.crypto.test.data

import com.crypto.test.data.dto.Balance
import com.crypto.test.data.dto.Currency
import com.crypto.test.data.dto.Tier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

/**
 * local data source is implemented as in-memory cache within this class for simplicity
 */
class WalletRepositoryImpl(
    private val remoteDataSource: WalletDataSource
) : WalletRepository {

    private var cachedRates: List<Tier>? = null
    private var cachedCurrencies: List<Currency>? = null
    private var cachedBalances: MutableMap<String, List<Balance>> = mutableMapOf()

    override suspend fun getRates(): Flow<List<Tier>> {
        return remoteDataSource.getRates()
            .onEach { cachedRates = it }
    }

    override suspend fun getCurrencies(): Flow<List<Currency>> {
        return remoteDataSource.getCurrencies()
            .onEach { cachedCurrencies = it }
    }

    override suspend fun getBalance(userToken: String): Flow<List<Balance>> {
        return remoteDataSource.getBalance(userToken)
            .onEach { cachedBalances[userToken] = it }
    }
}

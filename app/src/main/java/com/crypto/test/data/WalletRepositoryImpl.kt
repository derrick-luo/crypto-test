package com.crypto.test.data

import com.crypto.test.data.dto.Balance
import com.crypto.test.data.dto.Currency
import com.crypto.test.data.dto.Tier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.timeout
import kotlin.time.Duration.Companion.milliseconds

/**
 * local data source is implemented as in-memory cache within this class for simplicity
 */
@OptIn(FlowPreview::class)
class WalletRepositoryImpl(
    private val remoteDataSource: WalletDataSource
) : WalletRepository {

    private var cachedRates: List<Tier>? = null
    private var cachedCurrencies: List<Currency>? = null
    private var cachedBalances: MutableMap<String, List<Balance>> = mutableMapOf()

    override suspend fun getRates(): Flow<List<Tier>> {
        return remoteDataSource.getRates()
            .onEach { cachedRates = it }
            .timeout(1000.milliseconds)
            // we can handle other network exception here as well
            .catch { exception ->
                if (exception is TimeoutCancellationException) {
                    cachedRates?.let { emit(it) }
                } else {
                    throw exception
                }
            }
    }

    override suspend fun getCurrencies(): Flow<List<Currency>> {
        return remoteDataSource.getCurrencies()
            .onEach { cachedCurrencies = it }
            .timeout(1000.milliseconds)
            // we can handle other network exception here as well
            .catch { exception ->
                if (exception is TimeoutCancellationException) {
                    cachedCurrencies?.let { emit(it) }
                } else {
                    throw exception
                }
            }
    }

    override suspend fun getBalance(userToken: String): Flow<List<Balance>> {
        return remoteDataSource.getBalance(userToken)
            .onEach { cachedBalances[userToken] = it }
            .timeout(1000.milliseconds)
            // we can handle other network exception here as well
            .catch { exception ->
                if (exception is TimeoutCancellationException) {
                    cachedBalances[userToken]?.let { emit(it) }
                } else {
                    throw exception
                }
            }
    }
}

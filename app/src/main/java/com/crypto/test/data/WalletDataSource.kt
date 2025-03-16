package com.crypto.test.data

import com.crypto.test.data.dto.Balance
import com.crypto.test.data.dto.Currency
import com.crypto.test.data.dto.Tier
import kotlinx.coroutines.flow.Flow

interface WalletDataSource {

    suspend fun getRates(): Flow<List<Tier>>

    suspend fun getCurrencies(): Flow<List<Currency>>

    suspend fun getBalance(userToken: String): Flow<List<Balance>>
}

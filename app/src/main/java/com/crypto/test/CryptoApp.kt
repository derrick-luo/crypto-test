package com.crypto.test

import android.app.Activity
import android.app.Application
import com.crypto.test.data.RemoteWalletDataSource
import com.crypto.test.data.WalletRepository
import com.crypto.test.data.WalletRepositoryImpl

class CryptoApp : Application() {

    val walletRepo: WalletRepository by lazy {
        WalletRepositoryImpl(RemoteWalletDataSource())
    }

    companion object {
        fun get(activity: Activity): CryptoApp? {
            return activity.application as? CryptoApp
        }
    }
}

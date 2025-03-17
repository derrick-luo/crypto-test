package com.crypto.test

import android.app.Activity
import android.app.Application
import com.crypto.test.data.RemoteWalletDataSource
import com.crypto.test.data.UserRepository
import com.crypto.test.data.WalletRepository
import com.crypto.test.data.WalletRepositoryImpl

/**
 * we simply use app as an application scoped dependencies provider
 * for production build these can be implemented by hilt or koin DI frameworks
 */
class CryptoApp : Application() {

    val userRepo: UserRepository by lazy {
        object : UserRepository {}
    }

    val walletRepo: WalletRepository by lazy {
        WalletRepositoryImpl(RemoteWalletDataSource())
    }

    companion object {
        fun get(activity: Activity): CryptoApp? {
            return activity.application as? CryptoApp
        }
    }
}

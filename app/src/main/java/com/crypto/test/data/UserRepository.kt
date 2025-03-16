package com.crypto.test.data

interface UserRepository {

    fun getCurrentUserToken(): String {
        return "mocked-user-token"
    }
}

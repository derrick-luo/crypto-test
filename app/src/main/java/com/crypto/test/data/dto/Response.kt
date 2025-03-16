package com.crypto.test.data.dto

data class Response<T>(
    val ok: Boolean,
    val warning: String = "",
    val total: Int = 0,
    val data: T? = null
)

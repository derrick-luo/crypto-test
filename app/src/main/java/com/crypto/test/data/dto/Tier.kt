package com.crypto.test.data.dto

data class Tier(
    val fromCurrency: String,
    val toCurrency: String,
    val timestamp: Long,
    val rates: List<Rate> = emptyList()
)

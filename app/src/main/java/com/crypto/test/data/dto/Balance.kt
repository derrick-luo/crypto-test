package com.crypto.test.data.dto

import java.math.BigDecimal

data class Balance(
    val currency: String,
    val amount: BigDecimal
)

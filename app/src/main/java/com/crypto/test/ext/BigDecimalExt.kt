package com.crypto.test.ext

import java.math.BigDecimal
import java.math.RoundingMode

val BigDecimal.moneyStr: String
    get() = setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toEngineeringString()

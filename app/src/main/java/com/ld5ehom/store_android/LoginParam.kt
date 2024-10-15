package com.ld5ehom.store_android

import kotlinx.serialization.Serializable

@Serializable
data class LoginParam(
    val loginId: String,
    val password: String
)
package com.ld5ehom.sns_android

import kotlinx.serialization.Serializable

/**
 * @author Taewook.ok
 */
@Serializable
data class LoginParam(
    val loginId: String,
    val password: String
)
package com.ld5ehom.sns_android

import kotlinx.serialization.Serializable

/**
 * @author Taewook.ok
 */
@Serializable
data class NetworkResponse<T>(
    val result:String,
    val data:T,
    val errorMessage:String
)
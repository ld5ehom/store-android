package com.ld5ehom.store_android

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val result:String,
    val data:T,
    val errorMessage:String
)
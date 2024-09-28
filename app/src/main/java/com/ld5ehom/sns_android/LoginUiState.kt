package com.ld5ehom.sns_android

/**
 * @author Taewook.ok
 */
data class LoginUiState(
    val id:String,
    val pw:String,
    val userState:UserState = UserState.NONE
)
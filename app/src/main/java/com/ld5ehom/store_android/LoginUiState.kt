package com.ld5ehom.store_android


data class LoginUiState(
    val id:String,
    val pw:String,
    val userState:UserState = UserState.NONE
)
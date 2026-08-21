package com.infravo.bhaktmilan.ui.viewmodel

data class AuthUiState(

    val isLoading: Boolean = false,

    val otpSent: Boolean = false,

    val loginSuccess: Boolean = false,

    val isNewUser: Boolean = false,

    val isProfileCompleted: Boolean = false,

    val error: String? = null
)
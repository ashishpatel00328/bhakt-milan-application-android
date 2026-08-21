package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.ProfileDetail

data class MyProfileUiState(

    val isLoading: Boolean = false,

    val profile: ProfileDetail? = null,

    val error: String? = null

)
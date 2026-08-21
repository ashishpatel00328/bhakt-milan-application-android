package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.ui.model.ProfileListUiModel

data class ProfileUiState(

    val isLoading: Boolean = false,

    val profiles: List<ProfileListUiModel> = emptyList(),

    val error: String? = null
)
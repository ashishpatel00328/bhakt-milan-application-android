package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.Interest

data class InteractionUiState(

    val isLoading: Boolean = false,

    val isActionLoading: Boolean = false,

    val sentInterests: List<Interest> = emptyList(),

    val receivedInterests: List<Interest> = emptyList(),

    val lastActionSuccess: Boolean = false,

    val message: String? = null,

    val error: String? = null
)
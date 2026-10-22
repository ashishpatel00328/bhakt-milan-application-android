package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse


data class OnlineStatusUiState(
    val isLoading: Boolean = false,
    val status: OnlineStatusResponse? = null,
    val error: String? = null
)
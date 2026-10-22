
package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.ProfileDetail

data class ProfileDetailUiState(
    val isLoading: Boolean = false,
    val profile: ProfileDetail? = null,
    val error: String? = null,
    val is_active: Boolean? = null,

    // WhatsApp contact state
    val isContactLoading: Boolean = false,
    val whatsappNumber: String? = null,
    val contactError: String? = null
)
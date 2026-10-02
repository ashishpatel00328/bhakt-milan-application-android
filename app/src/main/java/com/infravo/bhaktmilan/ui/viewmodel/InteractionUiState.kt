package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.Interest
import com.infravo.bhaktmilan.data.remote.response.ShortlistedProfile

data class InteractionUiState(

    // =========================================================
    // GENERAL INTERACTION STATE
    // =========================================================

    val isLoading: Boolean = false,

    val isActionLoading: Boolean = false,

    val sentInterests: List<Interest> = emptyList(),

    val receivedInterests: List<Interest> = emptyList(),


    // =========================================================
    // SHORTLIST STATE
    // =========================================================

    val shortlistedProfiles: List<ShortlistedProfile> = emptyList(),

    val isShortlistLoading: Boolean = false,

    /*
     * IMPORTANT:
     *
     * Previously this was:
     *
     * isShortlistActionLoading: Boolean
     *
     * That made ALL profile cards show a loading/refresh
     * effect when only one profile was being shortlisted.
     *
     * Now we store the profileId currently being modified.
     *
     * Example:
     * BM1106 -> only BM1106 card shows loading.
     * Other cards remain unchanged.
     */
    val shortlistActionProfileId: String? = null,

    val shortlistError: String? = null,


    // =========================================================
    // LAST ACTION
    // =========================================================

    val lastActionSuccess: Boolean = false,

    val message: String? = null,

    val error: String? = null
)
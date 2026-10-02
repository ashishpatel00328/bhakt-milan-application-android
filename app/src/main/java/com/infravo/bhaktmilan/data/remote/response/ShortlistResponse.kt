package com.infravo.bhaktmilan.data.remote.response

data class ShortlistResponse(
    val success: Boolean,
    val message: String,
    val data: ShortlistData?
)

data class ShortlistData(
    val id: Int? = null,
    val profile_id: String? = null,
    val is_active: Boolean? = null
)
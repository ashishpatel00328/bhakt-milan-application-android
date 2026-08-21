package com.infravo.bhaktmilan.data.remote.response

import com.infravo.bhaktmilan.data.remote.response.MasterItem

data class MasterListResponse(
    val success: Boolean,
    val message: String,
    val data: List<MasterItem>
)
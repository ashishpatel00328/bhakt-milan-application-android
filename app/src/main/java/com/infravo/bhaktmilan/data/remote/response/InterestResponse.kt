package com.infravo.bhaktmilan.data.remote.response

data class InterestResponse(
    val success: Boolean,
    val message: String,
    val data: Interest
)

data class InterestListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Interest>
)

data class Interest(
    val id: Int,
    val sender_profile_pk: Int,
    val sender_profile_id: String,
    val sender_name: String,
    val receiver_profile_pk: Int,
    val receiver_profile_id: String,
    val receiver_name: String,
    val status: String,
    val message: String,
    val created_at: String,
    val responded_at: String?
)
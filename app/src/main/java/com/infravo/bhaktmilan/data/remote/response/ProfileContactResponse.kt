
package com.infravo.bhaktmilan.data.remote.response

data class ProfileContactResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ProfileContactData? = null
)

data class ProfileContactData(
    val profile_id: String? = null,
    val whatsapp_number: String? = null
)
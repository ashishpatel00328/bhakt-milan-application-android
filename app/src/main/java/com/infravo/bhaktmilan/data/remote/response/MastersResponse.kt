package com.infravo.bhaktmilan.data.remote.response

data class MastersResponse(
    val success: Boolean,
    val message: String,
    val data: MastersData
)

data class MastersData(
    val genders: List<MasterItem>,
    val marital_statuses: List<MasterItem>,
    val profile_managed_by: List<MasterItem>,
    val diet_preferences: List<MasterItem>,
    val blood_groups: List<MasterItem>,
    val disabilities: List<MasterItem>,
)

data class MasterItem(
    val id: Int,
    val code: String,
    val name: String
)


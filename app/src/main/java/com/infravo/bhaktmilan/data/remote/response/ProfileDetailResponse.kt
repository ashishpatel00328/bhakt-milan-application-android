package com.infravo.bhaktmilan.data.remote.response

data class ProfileDetailResponse(
    val success: Boolean,
    val message: String,
    val data: ProfileDetail
)

data class ProfileDetail(

    val id: Int?,

    val age: Int?,

    val is_active: Boolean?,

    val status: String?,

    val full_name: String?,

    val profile_photo: String?,

    val date_of_birth: String?,

    val height_cm: Int?,

    val weight_kg: Int?,

    val about_me: String?,

    val manglik: Boolean?,

    val annual_income: String?,

    val father_name: String?,

    val mother_name: String?,

    val married_brothers: Int?,

    val married_sisters: Int?,

    val unmarried_brothers: Int?,

    val unmarried_sisters: Int?,

    val family_is_satsangi: Boolean?,

    val birth_place: String?,

    val whatsapp_number: String?,

    val deleted_at: String?,

    val recoverable_until: String?,

    val gender: Int?,

    val blood_group: Int?,

    val marital_status: Int?,

    // Direct values from Profile Detail API
    val sampraday: String?,

    val guru: String?,

    val caste: String?,

    val sub_caste: String?,

    val gotra: String?,

    val nakshatra: String?,

    val zodiac: String?,

    val education: String?,

    val occupation: String?,

    val father_occupation: String?,

    // IDs - resolved using location/master data
    val country: Int?,

    val state: Int?,

    val city: Int?,

    // Direct value from API
    val mother_tongue: String?,


    val diet_preference: Int?,

    val disability: Int?,

    val profile_managed_by: Int?
)
package com.infravo.bhaktmilan.data.remote.request

data class UpdateProfileRequest(

    val full_name: String? = null,

    val profile_photo: String? = null,

    val gender: Int? = null,

    val date_of_birth: String? = null,

    val height_cm: Int? = null,

    val weight_kg: Int? = null,

    val blood_group: Int? = null,

    val marital_status: Int? = null,

    val about_me: String? = null,

    val sampraday: String? = null,

    val guru_name: String? = null,

    val caste: String? = null,

    val sub_caste: String? = null,

    val gotra: String? = null,

    val nakshatra: String? = null,

    val zodiac: String? = null,

    val manglik: Boolean? = null,

    val education: String? = null,

    val occupation: String? = null,

    val annual_income: String? = null,

    val father_occupation: String? = null,

    val father_name: String? = null,

    val mother_name: String? = null,

    val married_brothers: Int? = null,

    val married_sisters: Int? = null,

    val unmarried_brothers: Int? = null,

    val unmarried_sisters: Int? = null,

    val family_is_satsangi: Boolean? = null,

    val country: Int? = null,

    val state: Int? = null,

    val city: Int? = null,

    val birth_place: String? = null,

    val whatsapp_number: String? = null,

    val mother_tongue: String? = null,

    val diet_preference: Int? = null,

    val disability: Int? = null,

    val profile_managed_by: Int? = null
)
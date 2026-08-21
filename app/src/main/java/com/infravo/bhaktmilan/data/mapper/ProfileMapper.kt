package com.infravo.bhaktmilan.data.mapper

import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.data.remote.response.ProfileDetail
import com.infravo.bhaktmilan.data.remote.response.ProfileListItem
import com.infravo.bhaktmilan.ui.model.BhaktProfile

object ProfileMapper {

    // ==========================================
    // Profile List
    // ==========================================

    fun fromList(
        item: ProfileListItem
    ): BhaktProfile {

        return BhaktProfile(

            id = item.id,

            name = item.full_name,

            age = item.age,

            city = LocationCache.getCityName(
                stateId = item.state ?: 0,
                cityId = item.city
            ),

            sampraday = "",

            profession = item.occupation ?: "-",

            education = item.education ?: "-",

            maritalStatus = "",

            height = item.height_cm?.let {
                "${it} cm"
            } ?: "",

            diet = "",

            about = "",

            profileImageUrl = item.profile_photo.orEmpty(),

            gender = ""
        )
    }

    // ==========================================
    // Profile Detail
    // ==========================================

    fun fromDetail(
        item: ProfileDetail
    ): BhaktProfile {

        return BhaktProfile(

            id = item.id ?: 0,

            name = item.full_name ?: "-",

            age = item.age ?: 0,

            city = LocationCache.getCityName(
                stateId = item.state ?: 0,
                cityId = item.city
            ),

            // Direct API value
            sampraday = item.sampraday ?: "-",

            // Direct API value
            profession = item.occupation ?: "-",

            // Direct API value
            education = item.education ?: "-",

            maritalStatus = MastersCache.getMaritalStatusName(
                item.marital_status
            ),

            height = item.height_cm?.let {
                "${it} cm"
            } ?: "",

            diet = MastersCache.getDietPreferenceName(
                item.diet_preference
            ),

            about = item.about_me ?: "",

            profileImageUrl = item.profile_photo.orEmpty(),

            gender = MastersCache.getGenderName(
                item.gender
            )
        )
    }
}
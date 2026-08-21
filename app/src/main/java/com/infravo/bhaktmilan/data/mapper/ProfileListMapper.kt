package com.infravo.bhaktmilan.data.mapper

import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.response.ProfileListItem
import com.infravo.bhaktmilan.ui.model.ProfileListUiModel

fun ProfileListItem.toUiModel(): ProfileListUiModel {

    val cityName = if (city != null) {
        LocationCache.getCityName(
            stateId = state ?: 0,
            cityId = city
        )
    } else {
        ""
    }

    return ProfileListUiModel(

        id = id,

        profileId = profile_id,

        fullName = full_name,

        age = "$age Years",

        location = cityName,

        height = height_cm?.let {
            "$it cm"
        } ?: "-",

        education = education ?: "-",

        occupation = occupation ?: "-",

        profilePhoto = profile_photo
    )
}
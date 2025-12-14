package com.example.bhaktmilan.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BhaktProfile(
    val id: Int,
    val name: String,
    val age: Int,
    val city: String,
    val sampraday: String,
    val profession: String,
    val education: String,
    val maritalStatus: String,
    val height: String,
    val diet: String,
    val about: String,
    val profileImageUrl: String,
    val gender: String
) : Parcelable

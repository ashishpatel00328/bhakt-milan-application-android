package com.infravo.bhaktmilan.data.mapper

import com.infravo.bhaktmilan.data.remote.request.CreateProfileRequest
import com.infravo.bhaktmilan.data.remote.request.UpdateProfileRequest
import com.infravo.bhaktmilan.data.remote.response.ProfileDetail
import com.infravo.bhaktmilan.ui.model.OnboardingFormData

// ==========================================
// Create Profile
// ==========================================

fun OnboardingFormData.toCreateProfileRequest(): CreateProfileRequest {

    return CreateProfileRequest(

        // Required - Personal
        full_name = fullName,

        gender = gender
            ?: error("Gender is required"),

        date_of_birth = dateOfBirth,

        blood_group = bloodGroup
            ?: error("Blood Group is required"),

        marital_status = maritalStatus
            ?: error("Marital Status is required"),

        // Optional - Profile
        profile_photo = profilePhoto,

        // Optional - Physical
        height_cm = heightCm.toIntOrNull(),

        weight_kg = weightKg.toIntOrNull(),

        // Optional - About
        about_me = aboutMe.ifBlank { null },

        // Optional - Religious
        sampraday = sampraday.ifBlank { null },

        guru_name = guruName.ifBlank { null },

        caste = caste.ifBlank { null },

        sub_caste = subCaste.ifBlank { null },

        gotra = gotra.ifBlank { null },

        // Optional - Horoscope
        nakshatra = nakshatra.ifBlank { null },

        zodiac = zodiac.ifBlank { null },

        manglik = manglik,

        // Optional - Education & Career
        education = education.ifBlank { null },

        occupation = occupation.ifBlank { null },

        annual_income = annualIncome.ifBlank { null },

        // Optional - Family
        father_occupation =
            fatherOccupation.ifBlank { null },

        father_name =
            fatherName.ifBlank { null },

        mother_name =
            motherName.ifBlank { null },

        married_brothers = marriedBrothers,

        married_sisters = marriedSisters,

        unmarried_brothers = unmarriedBrothers,

        unmarried_sisters = unmarriedSisters,

        family_is_satsangi = familyIsSatsangi,

        // Required - Location
        country = country
            ?: error("Country is required"),

        state = state
            ?: error("State is required"),

        city = city
            ?: error("City is required"),

        // Optional - Other
        birth_place =
            birthPlace.ifBlank { null },

        whatsapp_number =
            whatsappNumber.ifBlank { null },

        mother_tongue =
            motherTongue.ifBlank { null },

        // Required - Lifestyle / Management
        diet_preference =
            dietPreference
                ?: error("Diet Preference is required"),

        disability =
            disability
                ?: error("Disability is required"),

        profile_managed_by =
            profileManagedBy
                ?: error("Profile Managed By is required")
    )
}

// ==========================================
// Profile Detail -> Edit Form
// ==========================================

fun ProfileDetail.toOnboardingFormData(): OnboardingFormData {

    return OnboardingFormData(

        // ==========================================
        // Required - Personal
        // ==========================================

        fullName = full_name.orEmpty(),

        gender = gender,

        dateOfBirth = date_of_birth.orEmpty(),

        bloodGroup = blood_group,

        maritalStatus = marital_status,

        // ==========================================
        // Required - Location
        // ==========================================

        country = country,

        state = state,

        city = city,

        // ==========================================
        // Required - Lifestyle / Management
        // ==========================================

        disability = disability,

        dietPreference = diet_preference,

        profileManagedBy = profile_managed_by,

        // ==========================================
        // Optional - Profile
        // ==========================================

        profilePhoto = profile_photo,

        // ==========================================
        // Optional - Physical
        // ==========================================

        heightCm = height_cm?.toString().orEmpty(),

        weightKg = weight_kg?.toString().orEmpty(),

        // ==========================================
        // Optional - About
        // ==========================================

        aboutMe = about_me.orEmpty(),

        // ==========================================
        // Optional - Religious
        // ==========================================

        sampraday = sampraday.orEmpty(),

        guruName = guru.orEmpty(),

        caste = caste.orEmpty(),

        subCaste = sub_caste.orEmpty(),

        gotra = gotra.orEmpty(),

        // ==========================================
        // Optional - Horoscope
        // ==========================================

        nakshatra = nakshatra.orEmpty(),

        zodiac = zodiac.orEmpty(),

        manglik = manglik,

        birthPlace = birth_place.orEmpty(),

        // ==========================================
        // Optional - Education & Career
        // ==========================================

        education = education.orEmpty(),

        occupation = occupation.orEmpty(),

        annualIncome = annual_income.orEmpty(),

        // ==========================================
        // Optional - Family
        // ==========================================

        fatherName = father_name.orEmpty(),

        fatherOccupation = father_occupation.orEmpty(),

        motherName = mother_name.orEmpty(),

        marriedBrothers = married_brothers,

        marriedSisters = married_sisters,

        unmarriedBrothers = unmarried_brothers,

        unmarriedSisters = unmarried_sisters,

        familyIsSatsangi = family_is_satsangi,

        // ==========================================
        // Optional - Contact
        // ==========================================

        whatsappNumber = whatsapp_number.orEmpty(),

        motherTongue = mother_tongue.orEmpty()
    )
}

fun OnboardingFormData.toUpdateProfileRequest(): UpdateProfileRequest {

    return UpdateProfileRequest(

        full_name = fullName.ifBlank { null },

        profile_photo = profilePhoto,

        gender = gender,

        date_of_birth = dateOfBirth.ifBlank { null },

        height_cm = heightCm.toIntOrNull(),

        weight_kg = weightKg.toIntOrNull(),

        blood_group = bloodGroup,

        marital_status = maritalStatus,

        about_me = aboutMe.ifBlank { null },

        sampraday = sampraday.ifBlank { null },

        guru_name = guruName.ifBlank { null },

        caste = caste.ifBlank { null },

        sub_caste = subCaste.ifBlank { null },

        gotra = gotra.ifBlank { null },

        nakshatra = nakshatra.ifBlank { null },

        zodiac = zodiac.ifBlank { null },

        manglik = manglik,

        education = education.ifBlank { null },

        occupation = occupation.ifBlank { null },

        annual_income = annualIncome.ifBlank { null },

        father_occupation = fatherOccupation.ifBlank { null },

        father_name = fatherName.ifBlank { null },

        mother_name = motherName.ifBlank { null },

        married_brothers = marriedBrothers,

        married_sisters = marriedSisters,

        unmarried_brothers = unmarriedBrothers,

        unmarried_sisters = unmarriedSisters,

        family_is_satsangi = familyIsSatsangi,

        country = country,

        state = state,

        city = city,

        birth_place = birthPlace.ifBlank { null },

        whatsapp_number = whatsappNumber.ifBlank { null },

        mother_tongue = motherTongue.ifBlank { null },

        diet_preference = dietPreference,

        disability = disability,

        profile_managed_by = profileManagedBy
    )
}
package com.infravo.bhaktmilan.data.mapper

import com.infravo.bhaktmilan.data.remote.request.CreateProfileRequest
import com.infravo.bhaktmilan.ui.model.OnboardingFormData

fun OnboardingFormData.toCreateProfileRequest(): CreateProfileRequest {

    return CreateProfileRequest(

        // ==========================================
        // Required - Personal
        // ==========================================

        full_name = fullName,

        gender = gender
            ?: error("Gender is required"),

        date_of_birth = dateOfBirth,

        blood_group = bloodGroup
            ?: error("Blood Group is required"),

        marital_status = maritalStatus
            ?: error("Marital Status is required"),

        // ==========================================
        // Optional - Profile
        // ==========================================

        profile_photo = profilePhoto,

        // ==========================================
        // Optional - Physical
        // ==========================================

        height_cm = heightCm.toIntOrNull(),

        weight_kg = weightKg.toIntOrNull(),

        // ==========================================
        // Optional - About
        // ==========================================

        about_me = aboutMe.ifBlank { null },

        // ==========================================
        // Optional - Religious
        // ==========================================

        sampraday = sampraday.ifBlank { null },

        guru_name = guruName.ifBlank { null },

        caste = caste.ifBlank { null },

        sub_caste = subCaste.ifBlank { null },

        gotra = gotra.ifBlank { null },

        // ==========================================
        // Optional - Horoscope
        // ==========================================

        nakshatra = nakshatra.ifBlank { null },

        zodiac = zodiac.ifBlank { null },

        manglik = manglik,

        // ==========================================
        // Optional - Education & Career
        // ==========================================

        education = education.ifBlank { null },

        occupation = occupation.ifBlank { null },

        annual_income = annualIncome.ifBlank { null },

        // ==========================================
        // Optional - Family
        // ==========================================

        father_occupation =
            fatherOccupation.ifBlank { null },

        father_name =
            fatherName.ifBlank { null },

        mother_name =
            motherName.ifBlank { null },

        married_brothers =
            marriedBrothers,

        married_sisters =
            marriedSisters,

        unmarried_brothers =
            unmarriedBrothers,

        unmarried_sisters =
            unmarriedSisters,

        family_is_satsangi =
            familyIsSatsangi,

        // ==========================================
        // Required - Location
        // ==========================================

        country = country
            ?: error("Country is required"),

        state = state
            ?: error("State is required"),

        city = city
            ?: error("City is required"),

        // ==========================================
        // Optional - Other
        // ==========================================

        birth_place =
            birthPlace.ifBlank { null },

        whatsapp_number =
            whatsappNumber.ifBlank { null },

        mother_tongue =
            motherTongue.ifBlank { null },

        // ==========================================
        // Required - Lifestyle / Management
        // ==========================================

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
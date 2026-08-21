package com.infravo.bhaktmilan.ui.model

data class OnboardingFormData(

    // ==========================================
    // Required - Personal
    // ==========================================

    var fullName: String = "",

    var gender: Int? = null,

    var dateOfBirth: String = "",

    var bloodGroup: Int? = null,

    var maritalStatus: Int? = null,

    // ==========================================
    // Required - Location
    // ==========================================

    var country: Int? = null,

    var state: Int? = null,

    var city: Int? = null,

    // ==========================================
    // Required - Lifestyle / Management
    // ==========================================

    var disability: Int? = null,

    var dietPreference: Int? = null,

    var profileManagedBy: Int? = null,

    // ==========================================
    // Optional - Profile Photo
    // ==========================================

    var profilePhoto: String? = null,

    // ==========================================
    // Optional - Physical
    // ==========================================

    var heightCm: String = "",

    var weightKg: String = "",

    // ==========================================
    // Optional - About
    // ==========================================

    var aboutMe: String = "",

    // ==========================================
    // Optional - Religious
    // ==========================================

    var sampraday: String = "",

    var guruName: String = "",

    var caste: String = "",

    var subCaste: String = "",

    var gotra: String = "",

    // ==========================================
    // Optional - Horoscope
    // ==========================================

    var nakshatra: String = "",

    var zodiac: String = "",

    var manglik: Boolean? = null,

    var birthPlace: String = "",

    // ==========================================
    // Optional - Education & Career
    // ==========================================

    var education: String = "",

    var occupation: String = "",

    var annualIncome: String = "",

    // ==========================================
    // Optional - Family
    // ==========================================

    var fatherName: String = "",

    var fatherOccupation: String = "",

    var motherName: String = "",

    var marriedBrothers: Int? = null,

    var marriedSisters: Int? = null,

    var unmarriedBrothers: Int? = null,

    var unmarriedSisters: Int? = null,

    var familyIsSatsangi: Boolean? = null,

    // ==========================================
    // Optional - Contact
    // ==========================================

    var whatsappNumber: String = "",

    var motherTongue: String = "",

    // ==========================================
    // Optional - Partner Preference
    // ==========================================

    var partnerPreference: String = ""
)
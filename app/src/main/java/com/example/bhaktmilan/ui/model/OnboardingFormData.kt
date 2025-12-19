package com.example.bhaktmilan.ui.model

data class OnboardingFormData(
    // Personal
    var profileManagedBy: String = "",
    var name: String = "",
    var dob: String = "",
    var age: String = "",
    var bloodGroup: String = "",
    var disability: String = "",

    // Contact
    var whatsapp: String = "",
    var state: String = "",
    var city: String = "",

    // Physical
    var height: String = "",
    var weight: String = "",
    var diet: String = "",

    // Religious
    var sampraday: String = "",
    var guruName: String = "",
    var familySatsangi: String = "",

    // Horoscope
    var zodiac: String = "",
    var nakshatra: String = "",
    var manglik: String = "",
    var birthPlace: String = "",

    // Community
    var caste: String = "",
    var subCaste: String = "",
    var gotra: String = "",
    var motherTongue: String = "",

    // Education & Career
    var qualification: String = "",
    var occupation: String = "",
    var annualIncome: String = "",

    // About
    var aboutMe: String = "",

    // Family
    var fatherName: String = "",
    var fatherOccupation: String = "",
    var motherName: String = "",
    var marriedSiblings: String = "",
    var unmarriedSiblings: String = "",

    // Partner
    var partnerPreference: String = ""
)

package com.infravo.bhaktmilan.data.remote.cache

import com.infravo.bhaktmilan.data.remote.response.MasterItem
import com.infravo.bhaktmilan.data.remote.response.MastersData

object MastersCache {

    private var isInitialized = false

    // ==========================================
    // Bootstrap Masters - Raw Items
    // ==========================================

    private var genderItems: List<MasterItem> = emptyList()
    private var maritalStatusItems: List<MasterItem> = emptyList()
    private var profileManagedByItems: List<MasterItem> = emptyList()
    private var dietPreferenceItems: List<MasterItem> = emptyList()
    private var bloodGroupItems: List<MasterItem> = emptyList()
    private var disabilityItems: List<MasterItem> = emptyList()

    // ==========================================
    // Bootstrap Masters - Lookup Maps
    // ==========================================

    private var genderMap: Map<Int, String> = emptyMap()
    private var maritalStatusMap: Map<Int, String> = emptyMap()
    private var profileManagedByMap: Map<Int, String> = emptyMap()
    private var dietPreferenceMap: Map<Int, String> = emptyMap()
    private var bloodGroupMap: Map<Int, String> = emptyMap()
    private var disabilityMap: Map<Int, String> = emptyMap()

    // ==========================================
    // Save Bootstrap Masters
    // ==========================================

    fun save(data: MastersData) {

        genderItems = data.genders
        maritalStatusItems = data.marital_statuses
        profileManagedByItems = data.profile_managed_by
        dietPreferenceItems = data.diet_preferences
        bloodGroupItems = data.blood_groups
        disabilityItems = data.disabilities

        genderMap = genderItems.toMap()
        maritalStatusMap = maritalStatusItems.toMap()
        profileManagedByMap = profileManagedByItems.toMap()
        dietPreferenceMap = dietPreferenceItems.toMap()
        bloodGroupMap = bloodGroupItems.toMap()
        disabilityMap = disabilityItems.toMap()

        isInitialized = true
    }

    // ==========================================
    // Cache Status
    // ==========================================

    fun isLoaded(): Boolean {
        return isInitialized
    }

    // ==========================================
    // Raw Lists - For Dropdowns
    // ==========================================

    fun getGenders(): List<MasterItem> {
        return genderItems
    }

    fun getMaritalStatuses(): List<MasterItem> {
        return maritalStatusItems
    }

    fun getProfileManagedBy(): List<MasterItem> {
        return profileManagedByItems
    }

    fun getDietPreferences(): List<MasterItem> {
        return dietPreferenceItems
    }

    fun getBloodGroups(): List<MasterItem> {
        return bloodGroupItems
    }

    fun getDisabilities(): List<MasterItem> {
        return disabilityItems
    }

    // ==========================================
    // Name Lookup
    // ==========================================

    fun getGenderName(id: Int?): String {
        if (id == null) return ""
        return genderMap[id] ?: ""
    }

    fun getMaritalStatusName(id: Int?): String {
        if (id == null) return ""
        return maritalStatusMap[id] ?: ""
    }

    fun getProfileManagedByName(id: Int?): String {
        if (id == null) return ""
        return profileManagedByMap[id] ?: ""
    }

    fun getDietPreferenceName(id: Int?): String {
        if (id == null) return ""
        return dietPreferenceMap[id] ?: ""
    }

    fun getBloodGroupName(id: Int?): String {
        if (id == null) return ""
        return bloodGroupMap[id] ?: ""
    }

    fun getDisabilityName(id: Int?): String {
        if (id == null) return ""
        return disabilityMap[id] ?: ""
    }

    // ==========================================
    // Clear
    // ==========================================

    fun clear() {

        isInitialized = false

        genderItems = emptyList()
        maritalStatusItems = emptyList()
        profileManagedByItems = emptyList()
        dietPreferenceItems = emptyList()
        bloodGroupItems = emptyList()
        disabilityItems = emptyList()

        genderMap = emptyMap()
        maritalStatusMap = emptyMap()
        profileManagedByMap = emptyMap()
        dietPreferenceMap = emptyMap()
        bloodGroupMap = emptyMap()
        disabilityMap = emptyMap()
    }

    // ==========================================
    // Helpers
    // ==========================================

    private fun List<MasterItem>.toMap(): Map<Int, String> {
        return associate { item ->
            item.id to item.name
        }
    }
}
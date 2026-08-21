package com.infravo.bhaktmilan.data.remote.cache

import com.infravo.bhaktmilan.data.remote.response.MasterItem

object LocationCache {

    // ==========================================
    // Raw Items
    // ==========================================

    private var countryItems: List<MasterItem> = emptyList()

    // Key = CountryId
    private val stateItems =
        mutableMapOf<Int, List<MasterItem>>()

    // Key = StateId
    private val cityItems =
        mutableMapOf<Int, List<MasterItem>>()

    // ==========================================
    // Lookup Maps
    // ==========================================

    private var countryMap: Map<Int, String> = emptyMap()

    // Key = CountryId
    // Value = Map<StateId, StateName>
    private val stateMap =
        mutableMapOf<Int, Map<Int, String>>()

    // Key = StateId
    // Value = Map<CityId, CityName>
    private val cityMap =
        mutableMapOf<Int, Map<Int, String>>()

    // ==========================================
    // Save
    // ==========================================

    fun saveCountries(
        items: List<MasterItem>
    ) {

        countryItems = items

        countryMap =
            items.associate {
                it.id to it.name
            }
    }

    fun saveStates(
        countryId: Int,
        items: List<MasterItem>
    ) {

        stateItems[countryId] = items

        stateMap[countryId] =
            items.associate {
                it.id to it.name
            }
    }

    fun saveCities(
        stateId: Int,
        items: List<MasterItem>
    ) {

        cityItems[stateId] = items

        cityMap[stateId] =
            items.associate {
                it.id to it.name
            }
    }

    // ==========================================
    // Raw Lists - Dropdowns
    // ==========================================

    fun getCountryItems(): List<MasterItem> {
        return countryItems
    }

    fun getStateItems(
        countryId: Int
    ): List<MasterItem> {
        return stateItems[countryId]
            ?: emptyList()
    }

    fun getCityItems(
        stateId: Int
    ): List<MasterItem> {
        return cityItems[stateId]
            ?: emptyList()
    }

    // ==========================================
    // Existing Map APIs
    // ==========================================

    fun getCountries(): Map<Int, String> {
        return countryMap
    }

    fun getStates(
        countryId: Int
    ): Map<Int, String> {
        return stateMap[countryId]
            ?: emptyMap()
    }

    fun getCities(
        stateId: Int
    ): Map<Int, String> {
        return cityMap[stateId]
            ?: emptyMap()
    }

    // ==========================================
    // Names
    // ==========================================

    fun getCountryName(
        countryId: Int?
    ): String {

        if (countryId == null) {
            return ""
        }

        return countryMap[countryId]
            ?: ""
    }

    fun getStateName(
        countryId: Int?,
        stateId: Int?
    ): String {

        if (
            countryId == null ||
            stateId == null
        ) {
            return ""
        }

        return stateMap[countryId]
            ?.get(stateId)
            ?: ""
    }

    fun getCityName(
        stateId: Int?,
        cityId: Int?
    ): String {

        if (
            stateId == null ||
            cityId == null
        ) {
            return ""
        }

        return cityMap[stateId]
            ?.get(cityId)
            ?: ""
    }

    // ==========================================
    // Cache Status
    // ==========================================

    fun isCountriesLoaded(): Boolean {
        return countryItems.isNotEmpty()
    }

    fun hasStates(
        countryId: Int
    ): Boolean {
        return stateItems.containsKey(countryId)
    }

    fun hasCities(
        stateId: Int
    ): Boolean {
        return cityItems.containsKey(stateId)
    }

    // ==========================================
    // Clear
    // ==========================================

    fun clear() {

        countryItems = emptyList()

        stateItems.clear()
        cityItems.clear()

        countryMap = emptyMap()

        stateMap.clear()
        cityMap.clear()
    }
}
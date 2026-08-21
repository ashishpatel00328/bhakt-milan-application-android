package com.infravo.bhaktmilan.ui.viewmodel

data class LocationUiState(

    val isLoading: Boolean = false,

    val error: String? = null,

    val countries: Map<Int, String> = emptyMap(),

    val states: Map<Int, String> = emptyMap(),

    val cities: Map<Int, String> = emptyMap()

)
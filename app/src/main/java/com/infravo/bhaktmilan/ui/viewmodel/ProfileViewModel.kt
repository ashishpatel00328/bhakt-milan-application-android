package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.mapper.toUiModel
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Online status cache: userId -> online status response
    private val _onlineStatuses =
        MutableStateFlow<Map<Int, OnlineStatusResponse>>(emptyMap())

    val onlineStatuses: StateFlow<Map<Int, OnlineStatusResponse>> =
        _onlineStatuses.asStateFlow()

    // Prevent duplicate requests while a user's status is loading
    private val loadingStatusUserIds = mutableSetOf<Int>()

    init {
        loadProfiles()
    }

    fun loadProfiles(
        page: Int = 1,
        gender: String? = null,
        new: Boolean? = null,
        activeNow: Boolean? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (
                val result = repository.getProfiles(
                    page = page,
                    gender = gender,
                    new = new,
                    activeNow = activeNow
                )
            ) {
                is NetworkResult.Success -> {
                    val apiProfiles = result.data.data.results

                    android.util.Log.d(
                        "PROFILE_LIST_DEBUG",
                        "API SUCCESS: raw profile count=${apiProfiles.size}"
                    )

                    val mappedProfiles = apiProfiles.map { it.toUiModel() }

                    android.util.Log.d(
                        "PROFILE_LIST_DEBUG",
                        "MAPPED count=${mappedProfiles.size}"
                    )

                    android.util.Log.d(
                        "PROFILE_LIST_DEBUG",
                        "Mapped profile IDs=${
                            mappedProfiles.take(5).map { it.profileId }
                        }"
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profiles = mappedProfiles,
                        error = null
                    )

                    android.util.Log.d(
                        "PROFILE_LIST_DEBUG",
                        "STATE updated: profiles=${_uiState.value.profiles.size}"
                    )
                }

                is NetworkResult.Error -> {
                    android.util.Log.e(
                        "PROFILE_LIST_DEBUG",
                        "API ERROR: ${result.message}"
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    /**
     * Fetch online status for an individual user.
     * Presence API errors do not affect the profile list.
     */
    fun loadOnlineStatus(userId: Int) {
        android.util.Log.e(
            "PRESENCE_TRACE",
            "STEP 1: ViewModel called, userId=$userId"
        )
        if (userId <= 0) {
            android.util.Log.e("ONLINE_STATUS", "Invalid userId: $userId")
            return
        }

        if (userId in loadingStatusUserIds) return
        if (_onlineStatuses.value.containsKey(userId)) return

        loadingStatusUserIds.add(userId)

        viewModelScope.launch {
            try {
                android.util.Log.d(
                    "ONLINE_STATUS",
                    "Calling API for userId=$userId"
                )

                when (val result = repository.getOnlineStatus(userId)) {
                    is NetworkResult.Success -> {
                        android.util.Log.d(
                            "ONLINE_STATUS",
                            "API SUCCESS: ${result.data}"
                        )

                        _onlineStatuses.value =
                            _onlineStatuses.value + (userId to result.data)
                    }

                    is NetworkResult.Error -> {
                        android.util.Log.e(
                            "ONLINE_STATUS",
                            "API ERROR: $result"
                        )
                    }

                    is NetworkResult.Loading -> {
                        android.util.Log.d(
                            "ONLINE_STATUS",
                            "API LOADING: userId=$userId"
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e(
                    "ONLINE_STATUS",
                    "Exception for userId=$userId",
                    e
                )
            } finally {
                loadingStatusUserIds.remove(userId)
            }
        }
    }
    /**
     * Refresh the profile list.
     */
    fun refresh() {
        loadProfiles()
    }

    /**
     * Clear the profile-list error.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}

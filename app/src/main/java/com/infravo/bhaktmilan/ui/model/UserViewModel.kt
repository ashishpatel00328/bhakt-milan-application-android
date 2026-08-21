package com.infravo.bhaktmilan.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import com.infravo.bhaktmilan.data.remote.response.ProfileDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyProfileUiState(

    val isLoading: Boolean = false,

    val profile: ProfileDetail? = null,

    val error: String? = null

)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MyProfileUiState())

    val uiState: StateFlow<MyProfileUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // Load My Profile
    // ==========================================

    fun loadMyProfile() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (
                val result =
                    profileRepository.getMyProfile()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            profile = result.data.data
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Clear Error
    // ==========================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }

    // ==========================================
    // Clear State
    // ==========================================

    fun clear() {

        _uiState.value =
            MyProfileUiState()
    }
}
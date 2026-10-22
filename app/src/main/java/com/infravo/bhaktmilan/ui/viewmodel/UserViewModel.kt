package com.infravo.bhaktmilan.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.AuthRepository
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import com.infravo.bhaktmilan.ui.viewmodel.MyProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyProfileUiState())

    val uiState: StateFlow<MyProfileUiState> = _uiState.asStateFlow()

    // =========================================================
    // LOAD MY PROFILE
    // =========================================================

    fun loadMyProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = profileRepository.getMyProfile()) {
                is NetworkResult.Success -> {
                    val latestProfile = result.data.data

                    // Update only after the server returns a successful profile.
                    // A failed request never clears the previously displayed photo.
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = latestProfile,
                        error = null
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // =========================================================
    // UPLOAD PROFILE PHOTO
    // =========================================================

    fun uploadProfilePhoto(
        context: Context,
        imageUri: Uri
    ) {
        if (_uiState.value.isUploadingPhoto) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUploadingPhoto = true,
                photoUploadSuccess = null,
                photoUploadError = null
            )

            try {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(imageUri) ?: "image/jpeg"

                val imageBytes = contentResolver
                    .openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.readBytes() }

                if (imageBytes == null || imageBytes.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        photoUploadError = "Selected image could not be read."
                    )
                    return@launch
                }

                val requestBody = imageBytes.toRequestBody(
                    mimeType.toMediaTypeOrNull()
                )

                val fileName = when (mimeType.lowercase()) {
                    "image/png" -> "profile_photo.png"
                    "image/webp" -> "profile_photo.webp"
                    else -> "profile_photo.jpg"
                }

                val photoPart = MultipartBody.Part.createFormData(
                    "profile_photo",
                    fileName,
                    requestBody
                )

                when (val result = profileRepository.uploadProfilePhoto(photoPart)) {
                    is NetworkResult.Success -> {
                        val response = result.data
                        val photoPath = response.data?.photo

                        if (response.success && !photoPath.isNullOrBlank()) {
                            // Immediately update the visible profile from the upload response.
                            // Coil will load the returned path through the screen's URL resolver.
                            val currentProfile = _uiState.value.profile

                            _uiState.value = _uiState.value.copy(
                                isUploadingPhoto = false,
                                profile = currentProfile?.copy(
                                    profile_photo = photoPath
                                ),
                                photoUploadSuccess = response.message,
                                photoUploadError = null
                            )

                            // Then fetch canonical profile data from the backend.
                            // If refresh fails, loadMyProfile keeps the just-updated photo.
                            loadMyProfile()
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isUploadingPhoto = false,
                                photoUploadError = response.message.ifBlank {
                                    "Photo upload failed."
                                }
                            )
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e(
                            "ProfilePhotoUpload",
                            "Upload failed: ${result.message}"
                        )

                        // Keep existing profile/photo unchanged on upload failure.
                        _uiState.value = _uiState.value.copy(
                            isUploadingPhoto = false,
                            photoUploadError = result.message
                        )
                    }

                    is NetworkResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isUploadingPhoto = false
                        )
                    }
                }
            } catch (exception: Exception) {
                Log.e(
                    "ProfilePhotoUpload",
                    "Unexpected upload error",
                    exception
                )

                // Preserve the current profile and its photo on failure.
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    photoUploadError = exception.message
                        ?: "Unable to upload profile photo."
                )
            }
        }
    }

    // =========================================================
    // CLEAR PHOTO UPLOAD MESSAGES
    // =========================================================

    fun clearPhotoUploadMessages() {
        _uiState.value = _uiState.value.copy(
            photoUploadSuccess = null,
            photoUploadError = null
        )
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    fun logout() {
        viewModelScope.launch {
            /*
             * AuthRepository already handles TokenManager.clearTokens().
             * No API call is required for the current logout flow.
             */
            authRepository.logout()

            // Clear profile data so a previous user's photo is not retained.
            _uiState.value = MyProfileUiState()
        }
    }

    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // =========================================================
    // CLEAR STATE
    // =========================================================

    fun clear() {
        _uiState.value = MyProfileUiState()
    }
}

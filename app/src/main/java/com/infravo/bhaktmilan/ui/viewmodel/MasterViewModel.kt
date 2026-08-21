package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.MasterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterViewModel @Inject constructor(
    private val repository: MasterRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MasterUiState())

    val uiState: StateFlow<MasterUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // LOAD MASTERS
    // ==========================================

    fun loadMasters() {

        viewModelScope.launch {

            _uiState.value = MasterUiState(
                isLoading = true,
                error = null
            )

            when (
                val result = repository.getMasters()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = MasterUiState(
                        isLoading = false,
                        isLoaded = true
                    )
                }

                is NetworkResult.Error -> {

                    _uiState.value = MasterUiState(
                        isLoading = false,
                        error = result.message
                    )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // CLEAR ERROR
    // ==========================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }

    // ==========================================
    // CLEAR STATE
    // ==========================================

    fun clear() {

        _uiState.value = MasterUiState()
    }
}
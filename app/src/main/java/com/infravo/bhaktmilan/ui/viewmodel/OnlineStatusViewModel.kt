package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.remote.repository.OnlineStatusRepository
import com.infravo.bhaktmilan.ui.viewmodel.OnlineStatusUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnlineStatusViewModel @Inject constructor(
    private val repository: OnlineStatusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnlineStatusUiState())
    val uiState: StateFlow<OnlineStatusUiState> = _uiState.asStateFlow()

    fun loadStatus(userId: Int) {
        if (userId <= 0) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getOnlineStatus(userId)
                .onSuccess { status ->
                    _uiState.value = OnlineStatusUiState(
                        isLoading = false,
                        status = status
                    )
                }
                .onFailure { error ->
                    _uiState.value = OnlineStatusUiState(
                        isLoading = false,
                        error = error.message ?: "Status load nahi ho saka"
                    )
                }
        }
    }
}
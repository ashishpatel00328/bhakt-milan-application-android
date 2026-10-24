package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.infravo.bhaktmilan.data.websocket.PresenceRepository
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class PresenceViewModel @Inject constructor(
    private val presenceRepository: PresenceRepository
) : ViewModel() {

    val statuses: StateFlow<Map<Int, OnlineStatusResponse>> =
        presenceRepository.statuses

    fun setTrackedUsers(
        owner: String,
        userIds: Collection<Int>
    ) {
        presenceRepository.setTrackedUsers(owner, userIds)
    }
}
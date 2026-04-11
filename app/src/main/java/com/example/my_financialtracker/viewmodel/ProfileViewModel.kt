package com.example.my_financialtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_financialtracker.data.AppContainer
import com.example.my_financialtracker.ui.state.ProfileUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel : ViewModel() {
    private val auth = AppContainer.firebaseAuth
    private val preferences = AppContainer.userPreferencesRepository

    val uiState: StateFlow<ProfileUiState> = preferences.preferredCurrency
        .map { currency ->
            val user = auth.currentUser
            ProfileUiState(
                displayName = user?.displayName ?: user?.email ?: "User",
                email = user?.email.orEmpty(),
                preferredCurrency = currency,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState(),
        )

    fun signOut() {
        auth.signOut()
    }
}

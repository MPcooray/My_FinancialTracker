package com.example.my_financialtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_financialtracker.data.AppContainer
import com.example.my_financialtracker.model.GoalOverview
import com.example.my_financialtracker.repository.GoalRepository
import com.example.my_financialtracker.ui.state.GoalUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalViewModel(
    private val repository: GoalRepository = AppContainer.goalRepository,
) : ViewModel() {
    private val statusState = MutableStateFlow(GoalUiState())

    val uiState: StateFlow<GoalUiState> = combine(
        repository.observePrimaryGoal(),
        repository.observeGoals(),
        statusState,
    ) { overview, goals, status ->
        status.copy(
            overview = overview,
            goals = goals,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GoalUiState(),
        )

    fun addGoal(
        title: String,
        targetAmount: String,
        currentSaved: String,
        monthsToDeadline: String,
    ) {
        val parsedTarget = targetAmount.toDoubleOrNull()
        val parsedSaved = currentSaved.toDoubleOrNull() ?: 0.0
        val parsedMonths = monthsToDeadline.toIntOrNull()

        if (title.isBlank() || parsedTarget == null || parsedTarget <= 0.0 || parsedMonths == null || parsedMonths <= 0) {
            statusState.update { it.copy(message = "Enter a valid goal title, amount, and deadline.") }
            return
        }

        viewModelScope.launch {
            statusState.update { it.copy(isSaving = true, message = null) }
            repository.addGoal(
                title = title,
                targetAmount = parsedTarget,
                currentSaved = parsedSaved,
                monthsToDeadline = parsedMonths,
            ).onSuccess {
                statusState.update { it.copy(isSaving = false, message = "Goal added.") }
            }.onFailure { throwable ->
                statusState.update { it.copy(isSaving = false, message = throwable.message ?: "Could not add goal.") }
            }
        }
    }
}

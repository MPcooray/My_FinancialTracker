package com.example.my_financialtracker.ui.state

import com.example.my_financialtracker.model.GoalOverview

data class GoalUiState(
    val overview: GoalOverview? = null,
    val goals: List<GoalOverview> = emptyList(),
    val isSaving: Boolean = false,
    val message: String? = null,
)

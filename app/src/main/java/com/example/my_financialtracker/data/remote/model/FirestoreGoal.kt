package com.example.my_financialtracker.data.remote.model

data class FirestoreGoal(
    val id: String = "",
    val title: String = "",
    val targetAmountLkr: Double = 0.0,
    val currentSavedLkr: Double = 0.0,
    val deadlineAt: Long = 0L,
    val createdAt: Long = 0L,
)

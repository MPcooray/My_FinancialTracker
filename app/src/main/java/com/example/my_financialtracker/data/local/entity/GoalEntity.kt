package com.example.my_financialtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val targetAmountLkr: Double,
    val currentSavedLkr: Double,
    val deadlineAt: Long,
    val createdAt: Long,
)

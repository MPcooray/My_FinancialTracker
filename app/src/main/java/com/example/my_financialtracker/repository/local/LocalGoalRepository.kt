package com.example.my_financialtracker.repository.local

import com.example.my_financialtracker.data.currency.CurrencyConverter
import com.example.my_financialtracker.data.currency.ExchangeRateRepository
import com.example.my_financialtracker.data.local.dao.GoalDao
import com.example.my_financialtracker.data.local.entity.GoalEntity
import com.example.my_financialtracker.data.preferences.UserPreferencesRepository
import com.example.my_financialtracker.data.remote.FirestoreSyncService
import com.example.my_financialtracker.model.GoalOverview
import com.example.my_financialtracker.repository.GoalRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import java.util.UUID
import kotlin.math.max

class LocalGoalRepository(
    private val goalDao: GoalDao,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val firebaseAuth: FirebaseAuth,
    private val syncService: FirestoreSyncService,
) : GoalRepository {

    override fun observePrimaryGoal(): Flow<GoalOverview?> {
        return combine(
            goalDao.observePrimaryGoal(),
            userPreferencesRepository.preferredCurrency,
            exchangeRateRepository.ratesToLkr,
        ) { goal, preferredCurrency, rates ->
            goal?.toOverview(preferredCurrency, rates)
        }
    }

    override fun observeGoals(): Flow<List<GoalOverview>> {
        return combine(
            goalDao.observeAllGoals(),
            userPreferencesRepository.preferredCurrency,
            exchangeRateRepository.ratesToLkr,
        ) { goals, preferredCurrency, rates ->
            goals.map { it.toOverview(preferredCurrency, rates) }
        }
    }

    override suspend fun addGoal(
        title: String,
        targetAmount: Double,
        currentSaved: Double,
        monthsToDeadline: Int,
    ): Result<Unit> = runCatching {
        val goal = GoalEntity(
            id = "goal_${UUID.randomUUID()}",
            title = title.trim(),
            targetAmountLkr = targetAmount,
            currentSavedLkr = currentSaved,
            deadlineAt = monthsFromNow(monthsToDeadline),
            createdAt = System.currentTimeMillis(),
        )
        goalDao.upsert(goal)
        firebaseAuth.currentUser?.uid?.let { uid -> syncService.pushGoal(uid, goal) }
    }

    override suspend fun seedDemoGoalIfNeeded() {
        if (goalDao.getAllGoals().isNotEmpty()) return

        goalDao.upsertAll(
            listOf(
                GoalEntity(
                    id = "goal_macbook",
                    title = "MacBook Pro Fund",
                    targetAmountLkr = 490000.0,
                    currentSavedLkr = 11200.0,
                    deadlineAt = monthsFromNow(12),
                    createdAt = System.currentTimeMillis(),
                ),
                GoalEntity(
                    id = "goal_emergency",
                    title = "Emergency Buffer",
                    targetAmountLkr = 150000.0,
                    currentSavedLkr = 25000.0,
                    deadlineAt = monthsFromNow(8),
                    createdAt = System.currentTimeMillis(),
                ),
            ),
        )
    }

    private fun GoalEntity.toOverview(
        preferredCurrency: String,
        rates: Map<String, Double>,
    ): GoalOverview {
        val remainingLkr = max(targetAmountLkr - currentSavedLkr, 0.0)
        val monthsRemaining = monthsUntil(deadlineAt).coerceAtLeast(1)
        val progress = if (targetAmountLkr <= 0.0) 0f else (currentSavedLkr / targetAmountLkr).toFloat().coerceIn(0f, 1f)

        return GoalOverview(
            id = id,
            title = title,
            targetAmountLabel = formatDisplayCurrency(targetAmountLkr, preferredCurrency, rates),
            currentSavedLabel = formatDisplayCurrency(currentSavedLkr, preferredCurrency, rates),
            remainingAmountLabel = formatDisplayCurrency(remainingLkr, preferredCurrency, rates),
            deadlineLabel = "$monthsRemaining months remaining",
            monthlyNeedLabel = "Need about ${formatDisplayCurrency(remainingLkr / monthsRemaining, preferredCurrency, rates)} per month",
            progress = progress,
            isCompleted = remainingLkr <= 0.0,
        )
    }

    private fun monthsFromNow(months: Int): Long {
        return Calendar.getInstance().apply {
            add(Calendar.MONTH, months.coerceAtLeast(1))
        }.timeInMillis
    }

    private fun monthsUntil(deadlineAt: Long): Int {
        val now = Calendar.getInstance()
        val deadline = Calendar.getInstance().apply { timeInMillis = deadlineAt }
        val yearDiff = deadline.get(Calendar.YEAR) - now.get(Calendar.YEAR)
        val monthDiff = deadline.get(Calendar.MONTH) - now.get(Calendar.MONTH)
        return yearDiff * 12 + monthDiff
    }

    private fun formatDisplayCurrency(
        amountLkr: Double,
        preferredCurrency: String,
        rates: Map<String, Double>,
    ): String {
        val rateToLkr = rates[preferredCurrency.uppercase()] ?: 1.0
        val converted = CurrencyConverter.fromLkr(amountLkr, rateToLkr)
        return CurrencyConverter.format(converted, preferredCurrency)
    }
}

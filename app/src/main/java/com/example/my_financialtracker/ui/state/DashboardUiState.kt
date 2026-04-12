package com.example.my_financialtracker.ui.state

import com.example.my_financialtracker.model.SummaryCard
import com.example.my_financialtracker.model.ChartDatum
import com.example.my_financialtracker.model.GoalOverview
import com.example.my_financialtracker.model.InsightItem
import com.example.my_financialtracker.model.TransactionItem

data class DashboardUiState(
    val welcomeTitle: String = "Money overview",
    val summaryCards: List<SummaryCard> = emptyList(),
    val insightItems: List<InsightItem> = emptyList(),
    val expenseChart: List<ChartDatum> = emptyList(),
    val incomeChart: List<ChartDatum> = emptyList(),
    val spendingSplitChart: List<ChartDatum> = emptyList(),
    val spendVsLeftChart: List<ChartDatum> = emptyList(),
    val spendVsLeftMessage: String = "",
    val featuredGoal: GoalOverview? = null,
    val recentTransactions: List<TransactionItem> = emptyList(),
)

package com.example.my_financialtracker.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.my_financialtracker.R
import com.example.my_financialtracker.ui.components.AppScaffold
import com.example.my_financialtracker.ui.components.EmptyStateCard
import com.example.my_financialtracker.ui.components.FrostedBadge
import com.example.my_financialtracker.ui.components.GradientHeroCard
import com.example.my_financialtracker.ui.components.HorizontalBarChartCard
import com.example.my_financialtracker.ui.components.MetricCard
import com.example.my_financialtracker.ui.components.PieChartCard
import com.example.my_financialtracker.ui.components.QuickActionCard
import com.example.my_financialtracker.ui.components.TransactionHighlightRow
import com.example.my_financialtracker.ui.state.DashboardUiState

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onGoalClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBottomNavClick: (String) -> Unit,
    currentRoute: String,
) {
    val incomeSummary = uiState.summaryCards.getOrNull(0)
    val expenseSummary = uiState.summaryCards.getOrNull(1)
    val secondarySummaries = uiState.summaryCards.drop(2).take(2)

    AppScaffold(
        title = stringResource(R.string.dashboard_title),
        currentRoute = currentRoute,
        showBottomBar = true,
        onBottomNavClick = onBottomNavClick,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddExpenseClick,
                text = { Text(stringResource(R.string.dashboard_add_expense)) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
    ) { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                GradientHeroCard(
                    eyebrow = stringResource(R.string.dashboard_currency_chip),
                    title = incomeSummary?.title ?: "Income recorded",
                    amount = incomeSummary?.amountLabel ?: "LKR 0.00",
                    subtitle = expenseSummary?.let { "Spent: ${it.amountLabel}" } ?: "Spent: LKR 0.00",
                    modifier = Modifier.fillMaxWidth(),
                    accent = {
                        FrostedBadge(
                            text = stringResource(R.string.dashboard_currency_chip),
                            icon = Icons.Outlined.Settings,
                        )
                    },
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(
                        title = stringResource(R.string.dashboard_add_income),
                        description = stringResource(R.string.dashboard_income_card_copy),
                        icon = Icons.Outlined.ArrowOutward,
                        modifier = Modifier.weight(1f),
                        onClick = onAddIncomeClick,
                    )
                    QuickActionCard(
                        title = stringResource(R.string.dashboard_add_expense),
                        description = "Capture spending and stay ahead of your budget",
                        icon = Icons.Outlined.Payments,
                        modifier = Modifier.weight(1f),
                        onClick = onAddExpenseClick,
                    )
                }
            }

            items(secondarySummaries.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowItems.forEach { item ->
                        MetricCard(
                            title = item.title,
                            amount = item.amountLabel,
                            description = item.description,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (rowItems.size == 1) {
                        MetricCard(
                            title = stringResource(R.string.dashboard_goal_card_title),
                            amount = uiState.featuredGoal?.currentSavedLabel ?: "LKR 0.00",
                            description = stringResource(R.string.dashboard_goal_card_copy),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Quick access",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(
                        title = stringResource(R.string.dashboard_manage_entries),
                        description = "Review and edit your latest records",
                        icon = Icons.Outlined.Wallet,
                        modifier = Modifier.weight(1f),
                        onClick = onTransactionsClick,
                    )
                    QuickActionCard(
                        title = stringResource(R.string.dashboard_open_goal),
                        description = "Track savings progress and your next milestone",
                        icon = Icons.Outlined.Savings,
                        modifier = Modifier.weight(1f),
                        onClick = onGoalClick,
                    )
                }
            }

            item {
                Text(
                    text = "Recent activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (uiState.recentTransactions.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "No transactions yet",
                        subtitle = "Add your first income or expense to start building your financial picture.",
                    )
                }
            } else {
                items(uiState.recentTransactions.take(4)) { item ->
                    TransactionHighlightRow(item = item)
                }
            }

            uiState.featuredGoal?.let { goal ->
                item {
                    MetricCard(
                        title = goal.title,
                        amount = goal.currentSavedLabel,
                        description = "${stringResource(R.string.goal_remaining, goal.remainingAmountLabel)} · ${goal.deadlineLabel}",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            item {
                HorizontalBarChartCard(
                    title = stringResource(R.string.dashboard_expense_chart),
                    items = uiState.expenseChart,
                )
            }

            item {
                PieChartCard(
                    title = stringResource(R.string.dashboard_income_pie_chart),
                    items = uiState.incomeChart,
                )
            }

            item {
                HorizontalBarChartCard(
                    title = stringResource(R.string.dashboard_spending_split_chart),
                    items = uiState.spendingSplitChart,
                )
            }

            item {
                QuickActionCard(
                    title = stringResource(R.string.dashboard_profile),
                    description = "Manage profile, preferences, and app settings",
                    icon = Icons.Outlined.Settings,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSettingsClick,
                )
            }
        }
    }
}

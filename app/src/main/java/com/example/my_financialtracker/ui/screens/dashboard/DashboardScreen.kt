package com.example.my_financialtracker.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.my_financialtracker.R
import com.example.my_financialtracker.model.QuickAction
import com.example.my_financialtracker.ui.components.ActionCard
import com.example.my_financialtracker.ui.components.AppScaffold
import com.example.my_financialtracker.ui.components.HorizontalBarChartCard
import com.example.my_financialtracker.ui.components.PieChartCard
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
    val quickActions = listOf(
        QuickAction(
            stringResource(R.string.quick_action_income_title),
            stringResource(R.string.quick_action_income_copy),
        ),
        QuickAction(
            stringResource(R.string.quick_action_expense_title),
            stringResource(R.string.quick_action_expense_copy),
        ),
        QuickAction(
            stringResource(R.string.quick_action_mix_title),
            stringResource(R.string.quick_action_mix_copy),
        ),
    )

    AppScaffold(
        title = stringResource(R.string.dashboard_title),
        currentRoute = currentRoute,
        showBottomBar = true,
        onBottomNavClick = onBottomNavClick,
    ) { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF0C3C60), Color(0xFF127A8A), Color(0xFF59B6C5)),
                                ),
                            )
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AssistChip(
                            onClick = onSettingsClick,
                            label = { Text(stringResource(R.string.dashboard_currency_chip)) },
                            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        )
                        Text(
                            text = uiState.welcomeTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Text(
                            text = stringResource(R.string.dashboard_hero_copy),
                            color = Color(0xFFE6F7FA),
                        )
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onAddIncomeClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.dashboard_add_income)) }
                    Button(onClick = onAddExpenseClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.dashboard_add_expense)) }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionCard(
                        title = stringResource(R.string.dashboard_income_card_title),
                        description = stringResource(R.string.dashboard_income_card_copy),
                        icon = Icons.Outlined.Payments,
                        modifier = Modifier.weight(1f),
                    )
                    ActionCard(
                        title = stringResource(R.string.dashboard_goal_card_title),
                        description = stringResource(R.string.dashboard_goal_card_copy),
                        icon = Icons.Outlined.Savings,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            items(uiState.summaryCards) { item ->
                Card(shape = RoundedCornerShape(22.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(item.title, fontWeight = FontWeight.SemiBold)
                        Text(item.amountLabel, style = MaterialTheme.typography.titleMedium)
                        Text(item.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
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
                    title = stringResource(R.string.dashboard_income_chart),
                    items = uiState.incomeChart,
                )
            }

            item {
                HorizontalBarChartCard(
                    title = stringResource(R.string.dashboard_spending_split_chart),
                    items = uiState.spendingSplitChart,
                )
            }

            uiState.featuredGoal?.let { goal ->
                item {
                    Card(shape = RoundedCornerShape(22.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(goal.title, fontWeight = FontWeight.SemiBold)
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = { goal.progress },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Text(
                                text = "${goal.currentSavedLabel} saved of ${goal.targetAmountLabel}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = stringResource(R.string.goal_remaining, goal.remainingAmountLabel),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            items(uiState.insightItems) { insight ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(insight.title, fontWeight = FontWeight.SemiBold)
                        Text(insight.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            items(quickActions) { action ->
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(action.title, fontWeight = FontWeight.SemiBold)
                        Text(action.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            items(uiState.recentTransactions.take(6)) { transaction ->
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(transaction.title, fontWeight = FontWeight.SemiBold)
                        Text(transaction.amountLabel)
                        Text(transaction.meta, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onTransactionsClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.dashboard_manage_entries)) }
                    Button(onClick = onGoalClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.dashboard_open_goal)) }
                }
            }

            item {
                Button(
                    onClick = onSettingsClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.dashboard_profile))
                }
            }
        }
    }
}

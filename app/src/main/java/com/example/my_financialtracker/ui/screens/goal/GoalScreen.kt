package com.example.my_financialtracker.ui.screens.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.my_financialtracker.R
import com.example.my_financialtracker.model.GoalOverview
import com.example.my_financialtracker.ui.components.AppScaffold
import com.example.my_financialtracker.ui.state.GoalUiState

@Composable
fun GoalScreen(
    uiState: GoalUiState,
    onAddGoal: (String, String, String, String) -> Unit,
    onBottomNavClick: (String) -> Unit,
    currentRoute: String,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    AppScaffold(
        title = stringResource(R.string.goal_title),
        currentRoute = currentRoute,
        showBottomBar = true,
        onBottomNavClick = onBottomNavClick,
    ) { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.goal_add_goal))
                    }
                }
            }

            item {
                uiState.message?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            items(uiState.goals) { goal ->
                GoalCard(goal = goal)
            }
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            isSaving = uiState.isSaving,
            onDismiss = { showAddDialog = false },
            onSave = { title, target, current, months ->
                onAddGoal(title, target, current, months)
                showAddDialog = false
            },
        )
    }
}

@Composable
private fun GoalCard(goal: GoalOverview) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            LinearProgressIndicator(
                progress = { goal.progress },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(stringResource(R.string.goal_target, goal.targetAmountLabel))
            Text(stringResource(R.string.goal_current, goal.currentSavedLabel))
            Text(stringResource(R.string.goal_remaining, goal.remainingAmountLabel))
            Text(stringResource(R.string.goal_deadline, goal.deadlineLabel))
            Text(goal.monthlyNeedLabel, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddGoalDialog(
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var currentSaved by remember { mutableStateOf("") }
    var monthsToDeadline by remember { mutableStateOf("12") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.goal_new_goal_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.field_name)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it },
                    label = { Text(stringResource(R.string.goal_target_amount_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = currentSaved,
                    onValueChange = { currentSaved = it },
                    label = { Text(stringResource(R.string.goal_current_saved_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = monthsToDeadline,
                    onValueChange = { monthsToDeadline = it },
                    label = { Text(stringResource(R.string.goal_months_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                enabled = !isSaving,
                onClick = { onSave(title, targetAmount, currentSaved, monthsToDeadline) },
            ) {
                Text(if (isSaving) stringResource(R.string.button_saving) else stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        },
    )
}

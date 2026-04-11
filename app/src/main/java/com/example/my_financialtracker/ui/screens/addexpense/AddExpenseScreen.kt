package com.example.my_financialtracker.ui.screens.addexpense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.my_financialtracker.R
import com.example.my_financialtracker.model.expenseCategories
import com.example.my_financialtracker.model.paymentMethods
import com.example.my_financialtracker.model.recurrenceOptions
import com.example.my_financialtracker.model.spendingTypes
import com.example.my_financialtracker.model.supportedCurrencies
import com.example.my_financialtracker.ui.components.AppScaffold
import com.example.my_financialtracker.ui.components.DropdownField
import com.example.my_financialtracker.ui.state.EntryFormUiState

@Composable
fun AddExpenseScreen(
    uiState: EntryFormUiState,
    onCategoryChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onRecurrenceChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onBottomNavClick: (String) -> Unit,
    currentRoute: String,
) {
    AppScaffold(
        title = stringResource(R.string.add_expense_title),
        currentRoute = currentRoute,
        showBottomBar = true,
        onBottomNavClick = onBottomNavClick,
    ) { modifier ->
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.add_expense_headline),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = uiState.helperText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            DropdownField(
                label = stringResource(R.string.field_category),
                value = uiState.primaryField,
                options = expenseCategories,
                onValueSelected = onCategoryChange,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = onAmountChange,
                label = { Text(stringResource(R.string.field_amount)) },
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownField(
                label = stringResource(R.string.field_currency),
                value = uiState.secondaryField,
                options = supportedCurrencies,
                onValueSelected = onCurrencyChange,
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownField(
                label = stringResource(R.string.field_spending_type),
                value = uiState.tertiaryField,
                options = spendingTypes,
                onValueSelected = onTypeChange,
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownField(
                label = stringResource(R.string.field_payment_method),
                value = uiState.quaternaryField,
                options = paymentMethods,
                onValueSelected = onPaymentMethodChange,
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownField(
                label = stringResource(R.string.field_recurrence),
                value = uiState.quinaryField,
                options = recurrenceOptions,
                onValueSelected = onRecurrenceChange,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = uiState.note,
                onValueChange = onNoteChange,
                label = { Text(stringResource(R.string.field_note)) },
                modifier = Modifier.fillMaxWidth(),
            )
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            uiState.successMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving,
            ) {
                Text(
                    if (uiState.isSaving) stringResource(R.string.button_saving)
                    else stringResource(R.string.button_save_expense),
                )
            }
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.button_back))
            }
        }
    }
}

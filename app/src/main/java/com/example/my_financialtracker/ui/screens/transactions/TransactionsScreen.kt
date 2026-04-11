package com.example.my_financialtracker.ui.screens.transactions

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
import com.example.my_financialtracker.model.AppDefaults
import com.example.my_financialtracker.model.TransactionItem
import com.example.my_financialtracker.model.TransactionType
import com.example.my_financialtracker.model.expenseCategories
import com.example.my_financialtracker.model.incomeSources
import com.example.my_financialtracker.model.paymentMethods
import com.example.my_financialtracker.model.spendingTypes
import com.example.my_financialtracker.model.supportedCurrencies
import com.example.my_financialtracker.ui.components.AppScaffold
import com.example.my_financialtracker.ui.components.DropdownField

@Composable
fun TransactionsScreen(
    transactions: List<TransactionItem>,
    message: String?,
    onUpdateTransaction: (TransactionItem) -> Unit,
    onDeleteTransaction: (TransactionItem) -> Unit,
    onConsumeMessage: () -> Unit,
    onBottomNavClick: (String) -> Unit,
    currentRoute: String,
) {
    var editingTransaction by remember { mutableStateOf<TransactionItem?>(null) }

    LaunchedEffect(message) {
        if (message != null) onConsumeMessage()
    }

    AppScaffold(
        title = stringResource(R.string.transactions_title),
        currentRoute = currentRoute,
        showBottomBar = true,
        onBottomNavClick = onBottomNavClick,
    ) { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.transactions_copy),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    message?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            items(transactions) { item ->
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(item.title, fontWeight = FontWeight.SemiBold)
                        Text(item.amountLabel)
                        Text(item.meta, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (item.note.isNotBlank()) {
                            Text(item.note, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = { editingTransaction = item }) {
                                Text(stringResource(R.string.button_edit))
                            }
                            TextButton(onClick = { onDeleteTransaction(item) }) {
                                Text(stringResource(R.string.button_delete))
                            }
                        }
                    }
                }
            }
        }
    }

    editingTransaction?.let { transaction ->
        EditTransactionDialog(
            transaction = transaction,
            onDismiss = { editingTransaction = null },
            onSave = {
                onUpdateTransaction(it)
                editingTransaction = null
            },
        )
    }
}

@Composable
private fun EditTransactionDialog(
    transaction: TransactionItem,
    onDismiss: () -> Unit,
    onSave: (TransactionItem) -> Unit,
) {
    var title by remember(transaction.id) { mutableStateOf(transaction.title) }
    var amount by remember(transaction.id) { mutableStateOf(transaction.originalAmount.toString()) }
    var currency by remember(transaction.id) { mutableStateOf(transaction.originalCurrency) }
    var spendingType by remember(transaction.id) { mutableStateOf(transaction.spendingType ?: AppDefaults.DEFAULT_SPENDING_TYPE) }
    var paymentMethod by remember(transaction.id) { mutableStateOf(transaction.paymentMethod ?: AppDefaults.DEFAULT_PAYMENT_METHOD) }
    var note by remember(transaction.id) { mutableStateOf(transaction.note) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_transaction_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = if (transaction.type == TransactionType.INCOME) {
                        stringResource(R.string.field_source)
                    } else {
                        stringResource(R.string.field_category)
                    },
                    value = title,
                    options = if (transaction.type == TransactionType.INCOME) incomeSources else expenseCategories,
                    onValueSelected = { title = it },
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.field_amount)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                DropdownField(
                    label = stringResource(R.string.field_currency),
                    value = currency,
                    options = supportedCurrencies,
                    onValueSelected = { currency = it },
                )
                if (transaction.type == TransactionType.EXPENSE) {
                    DropdownField(
                        label = stringResource(R.string.field_spending_type),
                        value = spendingType,
                        options = spendingTypes,
                        onValueSelected = { spendingType = it },
                    )
                    DropdownField(
                        label = stringResource(R.string.field_payment_method),
                        value = paymentMethod,
                        options = paymentMethods,
                        onValueSelected = { paymentMethod = it },
                    )
                }
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.field_note)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        transaction.copy(
                            title = title,
                            originalAmount = amount.toDoubleOrNull() ?: transaction.originalAmount,
                            originalCurrency = currency,
                            spendingType = if (transaction.type == TransactionType.EXPENSE) spendingType else null,
                            paymentMethod = if (transaction.type == TransactionType.EXPENSE) paymentMethod else null,
                            note = note,
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        },
    )
}

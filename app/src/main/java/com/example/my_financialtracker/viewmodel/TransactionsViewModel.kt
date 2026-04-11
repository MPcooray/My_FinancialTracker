package com.example.my_financialtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_financialtracker.data.AppContainer
import com.example.my_financialtracker.model.AppDefaults
import com.example.my_financialtracker.model.TransactionItem
import com.example.my_financialtracker.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val repository: FinanceRepository = AppContainer.financeRepository,
) : ViewModel() {
    val transactions: StateFlow<List<TransactionItem>> = repository.observeRecentTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun updateTransaction(transaction: TransactionItem) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
                .onSuccess { _message.value = AppDefaults.SUCCESS_TRANSACTION_UPDATED }
                .onFailure { _message.value = it.message ?: AppDefaults.ERROR_TRANSACTION_UPDATE }
        }
    }

    fun deleteTransaction(transaction: TransactionItem) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
                .onSuccess { _message.value = AppDefaults.SUCCESS_TRANSACTION_DELETED }
                .onFailure { _message.value = it.message ?: AppDefaults.ERROR_TRANSACTION_DELETE }
        }
    }

    fun consumeMessage() {
        _message.update { null }
    }
}

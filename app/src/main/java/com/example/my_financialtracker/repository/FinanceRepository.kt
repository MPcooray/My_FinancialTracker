package com.example.my_financialtracker.repository

import com.example.my_financialtracker.model.SummaryCard
import com.example.my_financialtracker.model.ChartDatum
import com.example.my_financialtracker.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun observeDashboardSummary(): Flow<List<SummaryCard>>
    fun observeExpenseChart(): Flow<List<ChartDatum>>
    fun observeIncomeChart(): Flow<List<ChartDatum>>
    fun observeRecentTransactions(): Flow<List<TransactionItem>>
    suspend fun seedDemoDataIfNeeded()
    suspend fun addIncome(
        sourceType: String,
        amount: Double,
        currency: String,
        note: String,
    ): Result<Unit>
    suspend fun addExpense(
        category: String,
        amount: Double,
        currency: String,
        spendingType: String,
        recurrenceType: String,
        paymentMethod: String,
        accountName: String,
        note: String,
    ): Result<Unit>
    suspend fun refreshRecurringExpensesIfNeeded()
    suspend fun updateTransaction(transaction: TransactionItem): Result<Unit>
    suspend fun deleteTransaction(transaction: TransactionItem): Result<Unit>
}

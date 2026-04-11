package com.example.my_financialtracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.my_financialtracker.data.local.dao.ExpenseDao
import com.example.my_financialtracker.data.local.dao.GoalDao
import com.example.my_financialtracker.data.local.dao.IncomeDao
import com.example.my_financialtracker.data.local.entity.ExpenseEntity
import com.example.my_financialtracker.data.local.entity.GoalEntity
import com.example.my_financialtracker.data.local.entity.IncomeEntity

@Database(
    entities = [IncomeEntity::class, ExpenseEntity::class, GoalEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getInstance(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finance_tracker.db",
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}

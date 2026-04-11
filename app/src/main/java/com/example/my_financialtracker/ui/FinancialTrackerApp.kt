package com.example.my_financialtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.my_financialtracker.navigation.FinancialTrackerNavHost

@Composable
fun FinancialTrackerApp() {
    val navController = rememberNavController()
    FinancialTrackerNavHost(navController = navController)
}

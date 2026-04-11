package com.example.my_financialtracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.my_financialtracker.R

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Dashboard : AppDestination("dashboard")
    data object AddIncome : AppDestination("add_income")
    data object AddExpense : AppDestination("add_expense")
    data object Transactions : AppDestination("transactions")
    data object Goal : AppDestination("goal")
    data object Settings : AppDestination("settings")
    data object Profile : AppDestination("profile")
}

data class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = AppDestination.Dashboard.route,
        labelRes = R.string.nav_home,
        icon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        route = AppDestination.AddExpense.route,
        labelRes = R.string.nav_expense,
        icon = Icons.Outlined.AddCard,
    ),
    BottomNavItem(
        route = AppDestination.Transactions.route,
        labelRes = R.string.nav_history,
        icon = Icons.Outlined.ReceiptLong,
    ),
    BottomNavItem(
        route = AppDestination.Goal.route,
        labelRes = R.string.nav_goal,
        icon = Icons.Outlined.Flag,
    ),
)

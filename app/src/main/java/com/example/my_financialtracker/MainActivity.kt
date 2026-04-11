package com.example.my_financialtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.my_financialtracker.ui.FinancialTrackerApp
import com.example.my_financialtracker.ui.theme.MyFinancialTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFinancialTrackerTheme {
                FinancialTrackerApp()
            }
        }
    }
}

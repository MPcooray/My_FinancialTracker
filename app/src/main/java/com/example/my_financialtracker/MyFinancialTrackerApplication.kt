package com.example.my_financialtracker

import android.app.Application
import com.example.my_financialtracker.data.AppContainer

class MyFinancialTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}

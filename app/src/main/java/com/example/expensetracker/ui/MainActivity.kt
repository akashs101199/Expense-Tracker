package com.example.expensetracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.expensetracker.components.BottomNavigationBar
import androidx.compose.foundation.layout.padding
import com.example.expensetracker.ui.view.AddExpenseScreen
import com.example.expensetracker.ui.view.CategoriesScreen
import com.example.expensetracker.ui.view.DashboardScreen
import com.example.expensetracker.ui.view.HistoryScreen
import com.example.expensetracker.ui.view.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerApp()
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") { DashboardScreen(navController) }
            composable("add") { AddExpenseScreen() }
            composable("history") { HistoryScreen() }
            composable("categories") { CategoriesScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

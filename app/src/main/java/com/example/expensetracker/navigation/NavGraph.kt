package com.example.expensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expensetracker.ui.view.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") { DashboardScreen(navController) }
        composable("add") { AddExpenseScreen() }
        composable("history") { HistoryScreen() }
        composable("categories") { CategoriesScreen() }
        composable("settings") { SettingsScreen() }
    }
}

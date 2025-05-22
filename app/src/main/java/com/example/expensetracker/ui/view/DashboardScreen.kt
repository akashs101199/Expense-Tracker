package com.example.expensetracker.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory

@Composable
fun DashboardScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as android.app.Application
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModelFactory(app))

    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    val expenses by viewModel.expenses.observeAsState(emptyList())
    val income by viewModel.income.observeAsState(0f)
    val totalSpent = expenses.sumOf { it.amount.toDouble() }
    val recentExpenses = expenses.sortedByDescending { it.date }.take(10)

    var showMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val filteredExpenses = if (selectedCategory == null) {
        recentExpenses
    } else {
        recentExpenses.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dashboard", style = MaterialTheme.typography.headlineSmall)

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {
                            showMenu = false
                            navController.navigate("settings")
                        }
                    )
                }
            }
        }

        // Greeting
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Hello, Satwik 👋", style = MaterialTheme.typography.titleMedium)
                Text("Track your money smartly", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Overview Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OverviewCard(
                title = "Spent",
                value = "₹${totalSpent.toInt()}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            OverviewCard(
                title = "Budget Left",
                value = "₹${(income - totalSpent).toInt()}",
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            OverviewCard(
                title = "Income",
                value = "₹${income.toInt()}",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Top Categories
        Text("Top Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Food", "Entertainment", "Utilities").forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = if (selectedCategory == category) null else category
                    },
                    label = {
                        Text(category, style = MaterialTheme.typography.bodyLarge)
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recent Transactions (Filtered)
        Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (filteredExpenses.isEmpty()) {
            Text("No recent transactions.")
        } else {
            filteredExpenses.forEach {
                TransactionItem(
                    title = it.note.ifBlank { "Expense" },
                    category = it.category,
                    time = it.date,
                    amount = "₹${it.amount.toInt()}"
                )
            }
        }
    }
}

@Composable
fun OverviewCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.bodyLarge, color = color)
        }
    }
}

@Composable
fun TransactionItem(title: String, category: String, time: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text("$category · $time", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(amount, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
    }
}

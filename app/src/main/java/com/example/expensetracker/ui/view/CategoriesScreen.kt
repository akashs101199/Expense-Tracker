package com.example.expensetracker.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory
import kotlin.math.roundToInt

data class CategoryExpense(val name: String, val amount: Float, val color: Color)

@Composable
fun CategoriesScreen() {
    val app = LocalContext.current.applicationContext as android.app.Application
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModelFactory(app))

    // ✅ Load expenses once on screen entry
    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    val categoryExpenses by viewModel.getCategoryBreakdown().observeAsState(emptyList())
    val total = categoryExpenses.sumOf { it.amount.toDouble() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spending by Category",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Total: ₹${total.roundToInt()} (${categoryExpenses.size} categories)")

        Spacer(modifier = Modifier.height(16.dp))

        if (categoryExpenses.isNotEmpty() && total > 0.0) {
            PieChart(categoryExpenses)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Breakdown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            CategoryDetails(categoryExpenses, total)
        } else {
            Text("No expenses to show.")
        }
    }
}

@Composable
fun PieChart(expenses: List<CategoryExpense>) {
    val total = expenses.sumOf { it.amount.toDouble() }
    if (total == 0.0) return

    var startAngle = -90f

    Canvas(
        modifier = Modifier
            .size(220.dp)
            .padding(8.dp)
    ) {
        val size = Size(size.width, size.height)

        expenses.forEach { expense ->
            val sweep = (expense.amount / total.toFloat()) * 360f

            drawArc(
                color = expense.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = size
            )

            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                size = size,
                style = Stroke(width = 2f)
            )

            startAngle += sweep
        }
    }
}

@Composable
fun CategoryDetails(expenses: List<CategoryExpense>, total: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        expenses.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(it.color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Text(
                    text = "₹${it.amount.roundToInt()}  (${((it.amount / total) * 100).roundToInt()}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

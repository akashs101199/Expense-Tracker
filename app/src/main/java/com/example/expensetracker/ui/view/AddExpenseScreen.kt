package com.example.expensetracker.ui.view

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen() {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var isRecurring by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val app = context.applicationContext as android.app.Application
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModelFactory(app))

    val calendar = Calendar.getInstance()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }

    val categories = listOf("Food", "Transport", "Entertainment", "Health", "Utilities")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add New Expense", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (₹)") },
            leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)") },
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Select Category", style = MaterialTheme.typography.titleMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        OutlinedButton(
            onClick = {
                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, y: Int, m: Int, d: Int ->
                        calendar.set(y, m, d)
                        selectedDate = dateFormat.format(calendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Date: $selectedDate")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Is this a recurring expense?")
            Switch(checked = isRecurring, onCheckedChange = { isRecurring = it })
        }

        Button(
            onClick = {
                if (amount.isNotBlank()) {
                    val expense = Expense(
                        amount = amount.toFloat(),
                        note = note,
                        category = selectedCategory,
                        date = selectedDate,
                        isRecurring = isRecurring
                    )
                    viewModel.addExpense(expense)
                    amount = ""
                    note = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Add Expense")
        }
    }
}

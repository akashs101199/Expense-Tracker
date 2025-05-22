package com.example.expensetracker.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory

@Composable
fun SettingsScreen() {
    val context = LocalContext.current.applicationContext as android.app.Application
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModelFactory(context))

    val income by viewModel.income.observeAsState(0f)
    val darkMode by viewModel.darkMode.observeAsState(false)

    var incomeInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)


        OutlinedTextField(
            value = incomeInput,
            onValueChange = { incomeInput = it },
            label = { Text("Set Monthly Income (₹)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (incomeInput.isNotBlank()) {
                    viewModel.saveIncome(incomeInput.toFloat())
                    incomeInput = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Income")
        }

        Text("Current Income: ₹${income.toInt()}", style = MaterialTheme.typography.bodyLarge)

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enable Dark Mode", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = darkMode,
                onCheckedChange = { viewModel.saveDarkMode(it) }
            )
        }
    }
}

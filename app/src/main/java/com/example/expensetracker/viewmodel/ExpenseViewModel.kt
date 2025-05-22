package com.example.expensetracker.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.preferences.PreferenceManager
import com.example.expensetracker.data.repository.AppDatabase
import com.example.expensetracker.ui.view.CategoryExpense
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).expenseDao()
    private val preferences = PreferenceManager(application)

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    val income: LiveData<Float> = preferences.incomeFlow.asLiveData()
    val darkMode: LiveData<Boolean> = preferences.darkModeFlow.asLiveData()

    fun saveIncome(value: Float) {
        viewModelScope.launch {
            preferences.saveIncome(value)
        }
    }

    fun saveDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferences.saveDarkMode(enabled)
        }
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _expenses.value = dao.getAllExpenses()
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense)
            loadExpenses()
        }
    }

    fun getCategoryBreakdown(): LiveData<List<CategoryExpense>> {
        return expenses.map { list ->
            list
                .filter { it.amount > 0f }
                .groupBy { it.category }
                .map { (category, grouped) ->
                    CategoryExpense(
                        name = category,
                        amount = grouped.sumOf { it.amount.toDouble() }.toFloat(),
                        color = when (category) {
                            "Food" -> Color(0xFFEF5350)
                            "Transport" -> Color(0xFF42A5F5)
                            "Entertainment" -> Color(0xFFAB47BC)
                            "Health" -> Color(0xFF26A69A)
                            "Utilities" -> Color(0xFFFFA726)
                            else -> Color.Gray
                        }
                    )
                }
                .filter { it.amount > 0f }
        }
    }
}

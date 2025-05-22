package com.example.expensetracker.data.repository

import com.example.expensetracker.data.model.Expense

class ExpenseRepository(private val dao: ExpenseDao) {

    suspend fun insert(expense: Expense) {
        dao.insertExpense(expense)
    }

    suspend fun getAll(): List<Expense> {
        return dao.getAllExpenses()
    }

    suspend fun delete(expense: Expense) {
        dao.deleteExpense(expense)
    }
}

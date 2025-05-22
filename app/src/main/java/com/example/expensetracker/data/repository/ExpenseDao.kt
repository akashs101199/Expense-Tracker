package com.example.expensetracker.data.repository

import androidx.room.*
import com.example.expensetracker.data.model.Expense

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getAllExpenses(): List<Expense>

    @Delete
    suspend fun deleteExpense(expense: Expense)
}

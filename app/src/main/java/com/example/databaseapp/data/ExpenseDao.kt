package com.example.databaseapp.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    suspend fun getAll(): List<Expense>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    suspend fun getTotal(): Double
}
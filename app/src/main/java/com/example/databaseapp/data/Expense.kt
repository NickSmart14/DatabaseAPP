package com.example.databaseapp.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val category: String,
    val amount: Double,
    val createdAt: Long = System.currentTimeMillis()
)

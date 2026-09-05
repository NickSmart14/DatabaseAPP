package com.example.databaseapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.databaseapp.data.AppDatabase
import com.example.databaseapp.data.Expense
import com.example.databaseapp.data.ExpenseDao
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var expenseDao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnLoad = findViewById<Button>(R.id.btnLoad)
        val tvExpenses = findViewById<TextView>(R.id.tvExpenses)

        expenseDao = AppDatabase
            .getDatabase(applicationContext)
            .expenseDao()

        btnSave.setOnClickListener {
            val description = etDescription.text.toString().trim()
            val category = etCategory.text.toString().trim()
            val amount = etAmount.text.toString().toDoubleOrNull()

            if (description.isBlank()) {
                etDescription.error = "Enter a description"
                return@setOnClickListener
            }

            if (category.isBlank()) {
                etCategory.error = "Enter a category"
                return@setOnClickListener
            }

            if (amount == null || amount <= 0) {
                etAmount.error = "Enter a valid amount"
                return@setOnClickListener
            }

            val expense = Expense(
                description = description,
                category = category,
                amount = amount
            )

            lifecycleScope.launch {
                expenseDao.insert(expense)

                Toast.makeText(
                    this@MainActivity,
                    "Expense saved",
                    Toast.LENGTH_SHORT
                ).show()

                etDescription.text.clear()
                etCategory.text.clear()
                etAmount.text.clear()
            }
        }

        btnLoad.setOnClickListener {
            lifecycleScope.launch {
                val expenses = expenseDao.getAll()
                val total = expenseDao.getTotal()

                tvExpenses.text = if (expenses.isEmpty()) {
                    "No expenses saved yet"
                } else {
                    val rows = expenses.joinToString("\n") { expense ->
                        "${expense.description} | " +
                                "${expense.category} | " +
                                "R%.2f".format(expense.amount)
                    }

                    "Total: R%.2f\n\n%s".format(total, rows)
                }
            }

        }
    }
}
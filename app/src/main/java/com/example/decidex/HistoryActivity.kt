package com.example.decidex

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var lvHistory: ListView
    private lateinit var btnClearHistory: Button
    private lateinit var btnBack: Button
    private lateinit var db: DecisionDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        lvHistory = findViewById(R.id.lvHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        btnBack = findViewById(R.id.btnBack)
        db = DecisionDbHelper(this)

        loadHistory()

        // AlertDialog before clearing (Practical 8)
        btnClearHistory.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Are you sure you want to delete all past decisions?")
                .setPositiveButton("Yes, Clear") { _, _ ->
                    db.clearAll()
                    Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show()
                    loadHistory()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            finish()
        }
    }

    private fun loadHistory() {
        val history = db.getAllDecisions().ifEmpty {
            arrayListOf("No decisions saved yet.\nMake a decision first!")
        }
        lvHistory.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, history)
    }
}

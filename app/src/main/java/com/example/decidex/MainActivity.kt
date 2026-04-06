package com.example.decidex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartDecision: Button
    private lateinit var btnHistory: Button
    private lateinit var btnLogout: Button
    private lateinit var btnImportNotes: Button
    private lateinit var tvUserEmail: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        tvUserEmail = findViewById(R.id.tvUserEmail)
        btnStartDecision = findViewById(R.id.btnStartDecision)
        btnHistory = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogout)
        btnImportNotes = findViewById(R.id.btnImportNotes)

        tvUserEmail.text = "Logged in as: ${mAuth.currentUser?.email}"

        btnStartDecision.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }
        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        btnImportNotes.setOnClickListener {
            startActivity(Intent(this, FileReaderActivity::class.java))
        }

        // Dialog box before logout (Practical 8)
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    mAuth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}

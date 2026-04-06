package com.example.decidex

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {

    private lateinit var etOption: EditText
    private lateinit var btnAddOption: Button
    private lateinit var btnNext: Button
    private lateinit var lvOptions: ListView
    private val optionsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        etOption = findViewById(R.id.etOption)
        btnAddOption = findViewById(R.id.btnAddOption)
        btnNext = findViewById(R.id.btnNext)
        lvOptions = findViewById(R.id.lvOptions)

        // ArrayAdapter for ListView (Practical 3)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, optionsList)
        lvOptions.adapter = adapter

        btnAddOption.setOnClickListener {
            val option = etOption.text.toString().trim()
            when {
                option.isEmpty() -> Toast.makeText(this, "Please type an option first", Toast.LENGTH_SHORT).show()
                optionsList.size >= 4 -> Toast.makeText(this, "Maximum 4 options allowed", Toast.LENGTH_SHORT).show()
                else -> {
                    optionsList.add(option)
                    adapter.notifyDataSetChanged()
                    etOption.setText("")
                }
            }
        }

        btnNext.setOnClickListener {
            if (optionsList.size < 2) {
                Toast.makeText(this, "Please add at least 2 options", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, RatingActivity::class.java)
            intent.putStringArrayListExtra("options", optionsList)
            startActivity(intent)
        }
    }
}

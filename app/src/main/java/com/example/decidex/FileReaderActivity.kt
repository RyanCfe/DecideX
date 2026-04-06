package com.example.decidex

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

// Practical 4: File picker and text file reading
class FileReaderActivity : AppCompatActivity() {

    private lateinit var tvFileName: TextView
    private lateinit var tvFileContent: TextView
    private lateinit var btnPickFile: Button
    private lateinit var btnBack: Button

    companion object {
        private const val FILE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_reader)

        tvFileName = findViewById(R.id.tvFileName)
        tvFileContent = findViewById(R.id.tvFileContent)
        btnPickFile = findViewById(R.id.btnPickFile)
        btnBack = findViewById(R.id.btnBack)

        btnPickFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "text/plain"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(Intent.createChooser(intent, "Select a text file"), FILE_PICK_CODE)
        }

        btnBack.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { readTextFile(it) }
        }
    }

    private fun readTextFile(uri: Uri) {
        try {
            // Get file name
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) tvFileName.text = "File: ${cursor.getString(nameIndex)}"
                }
            }

            // Read file content using BufferedReader
            val content = contentResolver.openInputStream(uri)?.use { stream ->
                BufferedReader(InputStreamReader(stream)).readText()
            }
            tvFileContent.text = content ?: "File is empty"

        } catch (e: Exception) {
            Toast.makeText(this, "Could not read file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

package com.example.decidex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {

    private lateinit var btnProductivity: Button
    private lateinit var btnRelaxation: Button
    private lateinit var btnHealth: Button
    private lateinit var btnUrgLow: Button
    private lateinit var btnUrgMedium: Button
    private lateinit var btnUrgHigh: Button
    private lateinit var btnGetResult: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var spinnerMood: Spinner

    private var selectedGoal = ""
    private var selectedMood = ""
    private var selectedUrgency = ""

    private lateinit var options: ArrayList<String>
    private lateinit var importanceRatings: IntArray
    private lateinit var desireRatings: IntArray
    private lateinit var effortRatings: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        options = intent.getStringArrayListExtra("options")!!
        importanceRatings = intent.getIntArrayExtra("importanceRatings")!!
        desireRatings = intent.getIntArrayExtra("desireRatings")!!
        effortRatings = intent.getIntArrayExtra("effortRatings")!!

        progressBar = findViewById(R.id.progressBar)
        btnGetResult = findViewById(R.id.btnGetResult)
        spinnerMood = findViewById(R.id.spinnerMood)
        btnProductivity = findViewById(R.id.btnProductivity)
        btnRelaxation = findViewById(R.id.btnRelaxation)
        btnHealth = findViewById(R.id.btnHealth)
        btnUrgLow = findViewById(R.id.btnUrgLow)
        btnUrgMedium = findViewById(R.id.btnUrgMedium)
        btnUrgHigh = findViewById(R.id.btnUrgHigh)

        // Spinner from string-array resource (Practical 2)
        val moodAdapter = ArrayAdapter.createFromResource(this, R.array.mood_options, android.R.layout.simple_spinner_item)
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMood.adapter = moodAdapter

        // Spinner listener (Practical 2)
        spinnerMood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMood = if (position == 0) "" else parent.getItemAtPosition(position).toString()
                updateProgress()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Goal buttons
        btnProductivity.setOnClickListener { selectedGoal = "Productivity"; highlight(btnProductivity, btnRelaxation, btnHealth); updateProgress() }
        btnRelaxation.setOnClickListener  { selectedGoal = "Relaxation";  highlight(btnRelaxation, btnProductivity, btnHealth); updateProgress() }
        btnHealth.setOnClickListener      { selectedGoal = "Health";      highlight(btnHealth, btnProductivity, btnRelaxation); updateProgress() }

        // Urgency buttons
        btnUrgLow.setOnClickListener    { selectedUrgency = "Low";    highlight(btnUrgLow, btnUrgMedium, btnUrgHigh); updateProgress() }
        btnUrgMedium.setOnClickListener { selectedUrgency = "Medium"; highlight(btnUrgMedium, btnUrgLow, btnUrgHigh); updateProgress() }
        btnUrgHigh.setOnClickListener   { selectedUrgency = "High";   highlight(btnUrgHigh, btnUrgLow, btnUrgMedium); updateProgress() }

        btnGetResult.setOnClickListener {
            if (selectedGoal.isEmpty() || selectedMood.isEmpty() || selectedUrgency.isEmpty()) {
                Toast.makeText(this, "Please answer all questions including mood", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val scores = calculateScores()
            val bestIndex = scores.indices.maxByOrNull { scores[it] } ?: 0

            val intent = Intent(this, ResultActivity::class.java)
            intent.putStringArrayListExtra("options", options)
            intent.putExtra("scores", scores)
            intent.putExtra("bestIndex", bestIndex)
            intent.putExtra("goal", selectedGoal)
            intent.putExtra("mood", selectedMood)
            intent.putExtra("urgency", selectedUrgency)
            intent.putExtra("importanceRatings", importanceRatings)
            intent.putExtra("desireRatings", desireRatings)
            intent.putExtra("effortRatings", effortRatings)
            startActivity(intent)
        }
    }

    private fun highlight(selected: Button, b2: Button, b3: Button) {
        selected.setBackgroundColor(0xFF6200EE.toInt())
        b2.setBackgroundColor(0xFFBBBBBB.toInt())
        b3.setBackgroundColor(0xFFBBBBBB.toInt())
    }

    // Weighted scoring formula
    private fun calculateScores(): IntArray {
        val urgencyWeight = when (selectedUrgency) { "High" -> 3.0f; "Medium" -> 2.0f; else -> 1.0f }
        val energyWeight  = when (selectedMood) { "Motivated", "Excited" -> 1.5f; "Tired" -> 0.8f; else -> 1.2f }
        val effortPenalty = when (selectedMood) { "Tired" -> 2.0f; "Stressed" -> 1.5f; "Motivated", "Excited" -> 0.5f; else -> 1.0f }

        return IntArray(options.size) { i ->
            val importance = importanceRatings[i].toFloat()
            val desire     = desireRatings[i].toFloat()
            val effort     = effortRatings[i].toFloat()

            var score = (importance * urgencyWeight) + (desire * energyWeight) - (effort * effortPenalty)
            score += when (selectedGoal) {
                "Productivity" -> importance * 1.5f
                "Relaxation"   -> desire * 1.5f
                "Health"       -> (importance + desire) * 0.8f
                else -> 0f
            }
            maxOf(1, score.toInt())
        }
    }

    private fun updateProgress() {
        progressBar.progress = listOf(selectedGoal, selectedMood, selectedUrgency).count { it.isNotEmpty() }
    }
}

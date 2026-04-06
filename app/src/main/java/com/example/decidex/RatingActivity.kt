package com.example.decidex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RatingActivity : AppCompatActivity() {

    private lateinit var tvOptionName: TextView
    private lateinit var tvCurrentOption: TextView
    private lateinit var tvImportanceVal: TextView
    private lateinit var tvDesireVal: TextView
    private lateinit var tvEffortVal: TextView
    private lateinit var sbImportance: SeekBar
    private lateinit var sbDesire: SeekBar
    private lateinit var sbEffort: SeekBar
    private lateinit var btnNextOption: Button

    private lateinit var options: ArrayList<String>
    private var currentIndex = 0
    private lateinit var importanceRatings: IntArray
    private lateinit var desireRatings: IntArray
    private lateinit var effortRatings: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        options = getIntent().getStringArrayListExtra("options")!!
        importanceRatings = IntArray(options.size)
        desireRatings = IntArray(options.size)
        effortRatings = IntArray(options.size)

        tvOptionName = findViewById(R.id.tvOptionName)
        tvCurrentOption = findViewById(R.id.tvCurrentOption)
        tvImportanceVal = findViewById(R.id.tvImportanceVal)
        tvDesireVal = findViewById(R.id.tvDesireVal)
        tvEffortVal = findViewById(R.id.tvEffortVal)
        sbImportance = findViewById(R.id.sbImportance)
        sbDesire = findViewById(R.id.sbDesire)
        sbEffort = findViewById(R.id.sbEffort)
        btnNextOption = findViewById(R.id.btnNextOption)

        sbImportance.setOnSeekBarChangeListener(simpleListener { tvImportanceVal.text = "$it/10" })
        sbDesire.setOnSeekBarChangeListener(simpleListener { tvDesireVal.text = "$it/10" })
        sbEffort.setOnSeekBarChangeListener(simpleListener { tvEffortVal.text = "$it/10" })

        loadOption(currentIndex)

        btnNextOption.setOnClickListener {
            importanceRatings[currentIndex] = sbImportance.progress
            desireRatings[currentIndex] = sbDesire.progress
            effortRatings[currentIndex] = sbEffort.progress
            currentIndex++

            if (currentIndex < options.size) {
                loadOption(currentIndex)
            } else {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putStringArrayListExtra("options", options)
                intent.putExtra("importanceRatings", importanceRatings)
                intent.putExtra("desireRatings", desireRatings)
                intent.putExtra("effortRatings", effortRatings)
                startActivity(intent)
            }
        }
    }

    private fun loadOption(index: Int) {
        tvOptionName.text = "Rate option ${index + 1} of ${options.size}"
        tvCurrentOption.text = options[index]
        sbImportance.progress = 5
        sbDesire.progress = 5
        sbEffort.progress = 5
        tvImportanceVal.text = "5/10"
        tvDesireVal.text = "5/10"
        tvEffortVal.text = "5/10"
        btnNextOption.text = if (index == options.size - 1) "Done — Answer Questions" else "Next Option →"
    }

    // Helper to reduce SeekBar boilerplate
    private fun simpleListener(onChanged: (Int) -> Unit) = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) = onChanged(p)
        override fun onStartTrackingTouch(s: SeekBar?) {}
        override fun onStopTrackingTouch(s: SeekBar?) {}
    }
}

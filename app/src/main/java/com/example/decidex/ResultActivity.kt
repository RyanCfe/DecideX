package com.example.decidex

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.util.Linkify
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// Practical 5: Entire screen built dynamically in Kotlin — no XML layout used
class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options          = intent.getStringArrayListExtra("options")!!
        val scores           = intent.getIntArrayExtra("scores")!!
        val bestIndex        = intent.getIntExtra("bestIndex", 0)
        val goal             = intent.getStringExtra("goal") ?: ""
        val mood             = intent.getStringExtra("mood") ?: ""
        val urgency          = intent.getStringExtra("urgency") ?: ""
        val importanceRatings = intent.getIntArrayExtra("importanceRatings")!!
        val desireRatings    = intent.getIntArrayExtra("desireRatings")!!
        val effortRatings    = intent.getIntArrayExtra("effortRatings")!!

        val bestOption = options[bestIndex]

        // === DYNAMIC UI (Practical 5) ===
        val scrollView = ScrollView(this).apply { setBackgroundColor(Color.WHITE) }
        val main = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        // Title
        main.addView(TextView(this).apply {
            text = "Your Decision"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        })

        // Result box
        main.addView(TextView(this).apply {
            text = bestOption.uppercase()
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#6200EE"))
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.parseColor("#F3E5F5"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        })

        // Why label
        main.addView(TextView(this).apply {
            text = "Why this choice?"
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 8)
        })

        // Reason text with Linkify (Practical 3)
        val tvReason = TextView(this).apply {
            text = buildReason(bestOption, goal, mood, urgency,
                importanceRatings[bestIndex], desireRatings[bestIndex], effortRatings[bestIndex])
            textSize = 14f
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.parseColor("#EEEEEE"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        }
        Linkify.addLinks(tvReason, Linkify.WEB_URLS)
        main.addView(tvReason)

        // Score breakdown label
        main.addView(TextView(this).apply {
            text = "Score Breakdown"
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 8)
        })

        // GridLayout for scores (Practical 8)
        val grid = GridLayout(this).apply {
            columnCount = 4
            setBackgroundColor(Color.parseColor("#EEEEEE"))
            setPadding(16, 16, 16, 16)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        }

        fun addGridCell(text: String, bold: Boolean = false, purple: Boolean = false) {
            grid.addView(TextView(this).apply {
                this.text = text
                textSize = 12f
                if (bold) setTypeface(null, Typeface.BOLD)
                if (purple) setTextColor(Color.parseColor("#6200EE"))
                setPadding(8, 8, 8, 8)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            })
        }

        listOf("Option", "Imp", "Want", "Score").forEach { addGridCell(it, bold = true) }
        options.forEachIndexed { i, opt ->
            val isWinner = i == bestIndex
            addGridCell(if (isWinner) "$opt ✓" else opt, purple = isWinner)
            addGridCell("${importanceRatings[i]}/10", purple = isWinner)
            addGridCell("${desireRatings[i]}/10", purple = isWinner)
            addGridCell("${scores[i]}pts", purple = isWinner)
        }
        main.addView(grid)

        // Save button
        val btnSave = Button(this).apply {
            text = "Save to History"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 16) }
        }
        btnSave.setOnClickListener {
            val db = DecisionDbHelper(this)
            db.insertDecision(options.joinToString(", "), bestOption, goal, mood, urgency)
            Toast.makeText(this, "Saved to history!", Toast.LENGTH_SHORT).show()
            btnSave.isEnabled = false
            btnSave.text = "Saved ✓"
        }
        main.addView(btnSave)

        // Try Again button
        val btnRetry = Button(this).apply {
            text = "Try Again"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 8) }
        }
        btnRetry.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            finish()
        }
        main.addView(btnRetry)

        // Home button
        val btnHome = Button(this).apply {
            text = "Go to Home"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            finish()
        }
        main.addView(btnHome)

        scrollView.addView(main)
        setContentView(scrollView)
    }

    private fun buildReason(best: String, goal: String, mood: String, urgency: String,
                             importance: Int, desire: Int, effort: Int): String {
        return buildString {
            appendLine("DecideX recommends: $best\n")
            appendLine("Your ratings:")
            appendLine("• Importance: $importance/10")
            appendLine("• Desire: $desire/10")
            appendLine("• Effort: $effort/10\n")
            appendLine("Your situation:")
            appendLine("• Goal: $goal")
            appendLine("• Mood: $mood")
            appendLine("• Urgency: $urgency\n")
            if (urgency == "High") appendLine("⚠ High urgency boosted importance scores.")
            if (mood == "Tired")   appendLine("😴 Tired mood penalised high-effort options.")
            if (mood == "Stressed") appendLine("😰 Stressed mood — pick something manageable.")
            if (effort >= 8)       appendLine("⚡ High effort task — make sure you are ready.")
            if (desire >= 8)       appendLine("✨ You really want to do this — that counts a lot.")
        }
    }
}

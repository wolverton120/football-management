package com.sqlite.footballmanagement

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GoalsActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val db = DatabaseHelper(this)
        val goalsData = db.getTopGoalScorers()

        val tableLayout = findViewById<TableLayout>(R.id.goalsTable)
        val tvHeader = findViewById<TextView>(R.id.tvGoalsHeader)

        val headerText = "Top Goal Scorers"
        startLoopingAnimation(tvHeader, headerText, 12000)

        val headerRow = TableRow(this)
        val headers = arrayOf("Player ID", "Player Name", "Goals Scored")
        for (header in headers) {
            val textView = TextView(this).apply {
                text = header
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(resources.getColor(android.R.color.black, theme))
                setPadding(16, 16, 16, 16)
            }
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)

        for (player in goalsData) {
            val row = TableRow(this)
            val fields = arrayOf(
                player["player_id"] ?: "",
                player["player_name"] ?: "",
                player["goals_scored"] ?: ""
            )
            for (field in fields) {
                val textView = TextView(this).apply {
                    text = field
                    textSize = 16f
                    setTypeface(null, Typeface.NORMAL)
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    setPadding(16, 16, 16, 16)
                }
                row.addView(textView)
            }
            tableLayout.addView(row)
        }
    }

    private fun animateText(textView: TextView, text: String, delay: Long = 100) {
        val handler = Handler()
        var index = 0

        textView.text = ""

        val runnable = object : Runnable {
            override fun run() {
                if (index < text.length) {
                    textView.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, delay)
                }
            }
        }

        handler.post(runnable)
    }

    private fun startLoopingAnimation(textView: TextView, text: String, interval: Long) {
        animationRunnable = object : Runnable {
            override fun run() {
                animateText(textView, text)
                animationHandler.postDelayed(this, interval)
            }
        }

        animationRunnable?.let { animationHandler.post(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        animationRunnable?.let { animationHandler.removeCallbacks(it) }
    }
}


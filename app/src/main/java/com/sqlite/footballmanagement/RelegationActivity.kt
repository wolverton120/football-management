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

class RelegationActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relegation)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tvRelegationHeader = findViewById<TextView>(R.id.tvRelegationHeader)
        val tableLayout = findViewById<TableLayout>(R.id.relegationTable)

        val db = DatabaseHelper(this)
        val relegationData = db.getRelegationTeams()

        val headerText = "Relegation Table"
        startLoopingAnimation(tvRelegationHeader, headerText, 12000)

        val headerRow = TableRow(this)
        val headers = arrayOf("Rank", "Team ID", "Team Name", "Points", "Status")
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

        for (team in relegationData) {
            val row = TableRow(this)
            val fields = arrayOf(
                team["rank"] ?: "",
                team["team_id"] ?: "",
                team["team_name"] ?: "",
                team["points_earned"] ?: "",
                team["status"] ?: ""
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



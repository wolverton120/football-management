package com.sqlite.footballmanagement

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class StatRulesActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_rules)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tvHeader = findViewById<TextView>(R.id.tvHeader)
        val tvContent = findViewById<TextView>(R.id.tvContent)

        tvHeader.text = "STAT RULES"
        tvContent.text = """
            Team Rankings:
            Teams are ranked based on the highest points they have obtained to the lowest.

            Goal Difference:
            The goal difference of each team is calculated by subtracting the total goals conceded from the total goals scored. Teams are ranked from the highest goal difference to the lowest.

            Relegation:
            The bottom three teams with the least points are relegated from the league and are shown in this table.
        """.trimIndent()

        val headerText = "STAT RULES"
        startLoopingAnimation(tvHeader, headerText, 12000)
    }

    private fun animateText(textView: TextView, text: String, delay: Long = 140) {
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


package com.sqlite.footballmanagement

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TeamStatsActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_stats)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tvTeamStatsHeader = findViewById<TextView>(R.id.tvTeamStatsHeader)
        val btnRankings = findViewById<Button>(R.id.btnRankings)
        val btnGoalDifference = findViewById<Button>(R.id.btnGoalDifference)
        val btnRelegation = findViewById<Button>(R.id.btnRelegation)
        val btnStatRules = findViewById<Button>(R.id.btnStatRules)

        val headerText = "Team Stats"
        startLoopingAnimation(tvTeamStatsHeader, headerText, 12000)

        btnRankings.setOnClickListener {
            startActivity(Intent(this, RankingsActivity::class.java))
        }

        btnGoalDifference.setOnClickListener {
            startActivity(Intent(this, GoalDifferenceActivity::class.java))
        }

        btnRelegation.setOnClickListener {
            startActivity(Intent(this, RelegationActivity::class.java))
        }

        btnStatRules.setOnClickListener {
            startActivity(Intent(this, StatRulesActivity::class.java))
        }
    }

    private fun animateText(textView: TextView, text: String, delay: Long = 150) {
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




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

class PlayerStatsActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_stats)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val btnTopScorers = findViewById<Button>(R.id.btnTopScorers)
        val btnTopAssisters = findViewById<Button>(R.id.btnTopAssisters)
        val btnTopGoalkeepers = findViewById<Button>(R.id.btnTopGoalkeepers)
        val btnTopEarners = findViewById<Button>(R.id.btnTopEarners)
        val btnMostAged = findViewById<Button>(R.id.btnMostAged)
        val btnPlayerAwards = findViewById<Button>(R.id.btnPlayerAwards)
        val btnStatRules = findViewById<Button>(R.id.btnStatRules)
        val tvHeader = findViewById<TextView>(R.id.tvPlayerStatsHeader)

        btnTopScorers.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        btnTopAssisters.setOnClickListener {
            startActivity(Intent(this, AssistsActivity::class.java))
        }

        btnTopGoalkeepers.setOnClickListener {
            startActivity(Intent(this, GoalkeepersActivity::class.java))
        }

        btnTopEarners.setOnClickListener {
            startActivity(Intent(this, SalariesActivity::class.java))
        }

        btnMostAged.setOnClickListener {
            startActivity(Intent(this, AgedPlayersActivity::class.java))
        }

        btnPlayerAwards.setOnClickListener {
            startActivity(Intent(this, AwardsActivity::class.java))
        }

        btnStatRules.setOnClickListener {
            startActivity(Intent(this, StatExplanationActivity::class.java))
        }

        val headerText = "Player Stats"
        startLoopingAnimation(tvHeader, headerText, 12000)
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



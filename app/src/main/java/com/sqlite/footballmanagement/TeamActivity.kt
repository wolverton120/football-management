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

class TeamActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tvTeamHeader = findViewById<TextView>(R.id.tvTeamHeader)
        val btnAddTeam = findViewById<Button>(R.id.btnAddTeam)
        val btnShowTeamTable = findViewById<Button>(R.id.btnShowTeamTable)
        val btnShowTeamStatsTable = findViewById<Button>(R.id.btnTeamStats)
        val headerText = "Team Management"
        startLoopingAnimation(tvTeamHeader, headerText, 12000)

        btnAddTeam.setOnClickListener {
            startActivity(Intent(this, AddTeamActivity::class.java))
        }

        btnShowTeamTable.setOnClickListener {
            val intent = Intent(this, ShowTeamsActivity::class.java)
            startActivity(intent)
        }

        btnShowTeamStatsTable.setOnClickListener {
            val intent = Intent(this, TeamStatsActivity::class.java)
            startActivity(intent)
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



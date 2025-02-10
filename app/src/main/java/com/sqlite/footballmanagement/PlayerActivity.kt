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

class PlayerActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val btnAddPlayer = findViewById<Button>(R.id.btnAddPlayer)
        val btnShowPlayers = findViewById<Button>(R.id.btnShowPlayers)
        val btnShowPlayerStats = findViewById<Button>(R.id.btnPlayerStats)
        val tvHeader = findViewById<TextView>(R.id.tvPlayerHeader)

        btnAddPlayer.setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            startActivity(intent)
        }

        btnShowPlayers.setOnClickListener {
            val intent = Intent(this, ShowPlayersActivity::class.java)
            startActivity(intent)
        }

        btnShowPlayerStats.setOnClickListener {
            val intent = Intent(this, PlayerStatsActivity::class.java)
            startActivity(intent)
        }

        val headerText = "Player Management"
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



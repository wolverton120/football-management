package com.sqlite.footballmanagement

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class StatExplanationActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_explanation)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val statRulesText = """
            Top Goal Scorers, Assisters, and Goalkeepers:
            These tables rank players based on the most goals scored, most assists, and most clean sheets respectively, from highest to lowest.
            
            Top Earners:
            The league's top earners are ranked from the highest paid to the lowest.
            
            Most Aged:
            The league's oldest players are ranked based on age, from most aged to the least.
            
            Player Awards:
            At the end of the season, the player with the most goals is awarded the Golden Boot. 
            The player with the most assists receives the PFA Playmaker of the Year award, while the player with the most clean sheets is awarded the Golden Glove.
        """.trimIndent()

        val headerTextView = findViewById<TextView>(R.id.tvStatExplanationHeader)
        val statRulesTextView = findViewById<TextView>(R.id.statRulesTextView)

        headerTextView.text = "STAT RULES"
        statRulesTextView.text = statRulesText

        startHeaderAnimation(headerTextView, "STAT RULES", 100, 12000)
    }

    private fun startHeaderAnimation(textView: TextView, text: String, charDelay: Long, loopInterval: Long) {
        animationRunnable = object : Runnable {
            override fun run() {
                animateHeaderText(textView, text, charDelay) {
                    animationHandler.postDelayed(this, loopInterval)
                }
            }
        }
        animationHandler.post(animationRunnable!!)
    }

    private fun animateHeaderText(textView: TextView, text: String, delay: Long, onComplete: () -> Unit) {
        val handler = Handler()
        var index = 0
        textView.text = ""

        val charAnimation = object : Runnable {
            override fun run() {
                if (index < text.length) {
                    textView.text = text.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, delay)
                } else {
                    onComplete()
                }
            }
        }
        handler.post(charAnimation)
    }

    override fun onDestroy() {
        super.onDestroy()
        animationRunnable?.let { animationHandler.removeCallbacks(it) }
    }
}



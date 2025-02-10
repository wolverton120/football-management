// Project went gold on 31/01/2025. For being only the second ever app I wrote, this wasn't that hard compared to the hell that was implementing
// firebase for the first project. Done as one of the two projects I was given on level 3, term 1 on my university. BAIUST FTW
package com.sqlite.footballmanagement

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val animationHandler = Handler(Looper.getMainLooper())
    private var animationRunnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val btnTeam = findViewById<Button>(R.id.btnTeam)
        val btnPlayer = findViewById<Button>(R.id.btnPlayer)
        val btnCredits = findViewById<Button>(R.id.btnCredits)
        val btnGoat = findViewById<Button>(R.id.btnGoat)

        val welcomeMessage = "Welcome to Football Management System"
        startLoopingAnimation(welcomeText, welcomeMessage, 12000)

        btnTeam.setOnClickListener {
            startActivity(Intent(this, TeamActivity::class.java))
        }

        btnPlayer.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        btnCredits.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("This app is developed by Sadman Adib, CSE 16th batch, BAIUST. during 3-1.\nThis project is powered by SQLite.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_green_light, theme))
            positiveButton.textSize = 18f

            val messageView = dialog.findViewById<TextView>(android.R.id.message)
            messageView?.apply {
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 16f
                typeface = resources.getFont(R.font.sourgummy)
            }
        }

        btnGoat.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val customView = layoutInflater.inflate(R.layout.dialog_goat, null)

            val captionTextView = customView.findViewById<TextView>(R.id.txtCaption)
            val fullText = "Lionel Messi is the greatest player of all time."
            captionTextView.text = ""

            animateText(captionTextView, fullText)

            builder.setView(customView)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_green_light, theme))
            positiveButton.textSize = 18f
        }
    }

    private fun animateText(textView: TextView, text: String, delay: Long = 50) {
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





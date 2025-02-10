package com.sqlite.footballmanagement

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val etPlayerName = findViewById<EditText>(R.id.etPlayerName)
        val etPlayerAge = findViewById<EditText>(R.id.etPlayerAge)
        val etPlayerSalary = findViewById<EditText>(R.id.etSalary)
        val etPlayerGoalsScored = findViewById<EditText>(R.id.etGoalsScored)
        val etPlayerGoalsAssisted = findViewById<EditText>(R.id.etGoalsAssisted)
        val etPlayerCleanSheets = findViewById<EditText>(R.id.etMostCleanSheets)
        val btnAddPlayer = findViewById<Button>(R.id.btnAddPlayer)

        val db = DatabaseHelper(this)

        btnAddPlayer.setOnClickListener {
            val playerName = etPlayerName.text.toString()
            val playerAge = etPlayerAge.text.toString().toIntOrNull()
            val playerSalary = etPlayerSalary.text.toString().toDoubleOrNull()
            val playerGoalsScored = etPlayerGoalsScored.text.toString().toIntOrNull()
            val playerGoalsAssisted = etPlayerGoalsAssisted.text.toString().toIntOrNull()
            val playerCleanSheets = etPlayerCleanSheets.text.toString().toIntOrNull()

            if (playerAge != null && playerSalary != null && playerGoalsScored != null && playerGoalsAssisted != null && playerCleanSheets != null) {
                val contentValues = ContentValues().apply {
                    put("player_name", playerName)
                    put("age", playerAge)
                    put("salary", playerSalary)
                    put("goals_scored", playerGoalsScored)
                    put("goals_assisted", playerGoalsAssisted)
                    put("most_clean_sheets", playerCleanSheets)
                }

                val success = db.addPlayer(contentValues)
                if (success) {
                    Toast.makeText(this, "Player added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add player.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid input.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



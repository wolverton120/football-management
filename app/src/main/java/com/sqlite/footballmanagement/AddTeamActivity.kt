package com.sqlite.footballmanagement

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val etTeamName = findViewById<EditText>(R.id.etTeamName)
        val etMatchWon = findViewById<EditText>(R.id.etMatchWon)
        val etMatchLost = findViewById<EditText>(R.id.etMatchLost)
        val etMatchDrew = findViewById<EditText>(R.id.etMatchDrew)
        val etPointsEarned = findViewById<EditText>(R.id.etPointsEarned)
        val etGoalsScored = findViewById<EditText>(R.id.etGoalsScored)
        val etGoalsConceded = findViewById<EditText>(R.id.etGoalsConceded)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val db = DatabaseHelper(this)

        btnSubmit.setOnClickListener {
            val teamName = etTeamName.text.toString()
            val matchWon = etMatchWon.text.toString().toIntOrNull() ?: 0
            val matchLost = etMatchLost.text.toString().toIntOrNull() ?: 0
            val matchDrew = etMatchDrew.text.toString().toIntOrNull() ?: 0
            val pointsEarned = etPointsEarned.text.toString().toIntOrNull() ?: 0
            val goalsScored = etGoalsScored.text.toString().toIntOrNull() ?: 0
            val goalsConceded = etGoalsConceded.text.toString().toIntOrNull() ?: 0

            if (teamName.isNotBlank()) {
                val contentValues = ContentValues().apply {
                    put("team_name", teamName)
                    put("match_won", matchWon)
                    put("match_lost", matchLost)
                    put("match_drew", matchDrew)
                    put("points_earned", pointsEarned)
                    put("goals_scored", goalsScored)
                    put("goals_conceded", goalsConceded)
                }

                val success = db.addTeam(contentValues)
                if (success) {
                    Toast.makeText(this, "Team added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add team.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid team name.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



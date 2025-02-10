package com.sqlite.footballmanagement

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class EditTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team)
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
        val btnSubmitEdit = findViewById<Button>(R.id.btnSubmitEdit)

        val db = DatabaseHelper(this)

        val teamId = intent.getIntExtra("team_id", -1)

        if (teamId != -1) {
            val team = db.getTeamById(teamId)
            if (team != null) {
                etTeamName.setText(team["team_name"])
                etMatchWon.setText(team["match_won"])
                etMatchLost.setText(team["match_lost"])
                etMatchDrew.setText(team["match_drew"])
                etPointsEarned.setText(team["points_earned"])
                etGoalsScored.setText(team["goals_scored"])
                etGoalsConceded.setText(team["goals_conceded"])
            } else {
                Toast.makeText(this, "Team not found!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Invalid Team ID!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSubmitEdit.setOnClickListener {
            val teamName = etTeamName.text.toString()
            val matchWon = etMatchWon.text.toString().toIntOrNull()
            val matchLost = etMatchLost.text.toString().toIntOrNull()
            val matchDrew = etMatchDrew.text.toString().toIntOrNull()
            val pointsEarned = etPointsEarned.text.toString().toIntOrNull()
            val goalsScored = etGoalsScored.text.toString().toIntOrNull()
            val goalsConceded = etGoalsConceded.text.toString().toIntOrNull()

            if (matchWon != null && matchLost != null && matchDrew != null &&
                pointsEarned != null && goalsScored != null && goalsConceded != null) {
                val contentValues = ContentValues().apply {
                    put("team_name", teamName)
                    put("match_won", matchWon)
                    put("match_lost", matchLost)
                    put("match_drew", matchDrew)
                    put("points_earned", pointsEarned)
                    put("goals_scored", goalsScored)
                    put("goals_conceded", goalsConceded)
                }

                val success = db.editTeam(teamId, contentValues)
                if (success) {
                    Toast.makeText(this, "Team updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update team.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid input.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



package com.sqlite.footballmanagement

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ShowTeamsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_teams)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tableLayout = findViewById<TableLayout>(R.id.tableTeams)
        val dbHelper = DatabaseHelper(this)
        val teams = dbHelper.getAllTeams()
        val headerRow = TableRow(this)
        val headers = arrayOf("Team ID", "Team Name", "Match Won", "Match Lost", "Match Drew",
            "Points Earned", "Goals Scored", "Goals Conceded", "Edit", "Remove"
        )
        for (header in headers) {
            val textView = TextView(this).apply {
                text = header
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(resources.getColor(android.R.color.black, theme))
                setPadding(16, 16, 16, 16)
            }
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)

        for (team in teams) {
            val row = TableRow(this)

            val fields = arrayOf(
                team["team_id"] ?: "",
                team["team_name"] ?: "",
                team["match_won"] ?: "",
                team["match_lost"] ?: "",
                team["match_drew"] ?: "",
                team["points_earned"] ?: "",
                team["goals_scored"] ?: "",
                team["goals_conceded"] ?: ""
            )
            for (field in fields) {
                val textView = TextView(this).apply {
                    text = field
                    textSize = 16f
                    setTypeface(null, Typeface.NORMAL)
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    setPadding(16, 16, 16, 16)
                }
                row.addView(textView)
            }

            val editButton = Button(this).apply {
                text = "Edit"
                setOnClickListener {
                    val intent = Intent(this@ShowTeamsActivity, EditTeamActivity::class.java)
                    intent.putExtra("team_id", team["team_id"]?.toIntOrNull())
                    startActivity(intent)
                }
            }
            row.addView(editButton)

            val removeButton = Button(this).apply {
                text = "Remove"
                setOnClickListener {
                    val teamId = team["team_id"]?.toIntOrNull()
                    if (teamId != null) {
                        val success = dbHelper.removeTeam(teamId)
                        if (success) {
                            Toast.makeText(this@ShowTeamsActivity, "Team removed successfully!", Toast.LENGTH_SHORT).show()
                            tableLayout.removeView(row)
                        } else {
                            Toast.makeText(this@ShowTeamsActivity, "Failed to remove team.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            row.addView(removeButton)

            tableLayout.addView(row)
        }
    }
}





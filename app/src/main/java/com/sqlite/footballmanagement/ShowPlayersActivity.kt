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
import java.text.NumberFormat
import java.util.Locale

class ShowPlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_players)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val tableLayout = findViewById<TableLayout>(R.id.tablePlayers)
        val dbHelper = DatabaseHelper(this)
        val players = dbHelper.getAllPlayers()
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        val headerRow = TableRow(this)
        val headers = arrayOf("Player ID", "Player Name", "Age", "Salary", "Goals Scored", "Goals Assisted", "Clean Sheets", "Edit", "Remove")
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

        for (player in players) {
            val row = TableRow(this)

            val fields = arrayOf(
                player["player_id"] ?: "",
                player["player_name"] ?: "",
                player["age"] ?: "",
                player["salary"]?.toDoubleOrNull()?.let { formatter.format(it) } ?: "",
                player["goals_scored"] ?: "",
                player["goals_assisted"] ?: "",
                player["most_clean_sheets"] ?: ""
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
                    val intent = Intent(this@ShowPlayersActivity, EditPlayerActivity::class.java)
                    intent.putExtra("player_id", player["player_id"]?.toIntOrNull())
                    startActivity(intent)
                }
            }
            row.addView(editButton)

            val removeButton = Button(this).apply {
                text = "Remove"
                setOnClickListener {
                    val playerId = player["player_id"]?.toIntOrNull()
                    if (playerId != null) {
                        val db = DatabaseHelper(this@ShowPlayersActivity)
                        val success = db.removePlayer(playerId)
                        if (success) {
                            Toast.makeText(this@ShowPlayersActivity, "Player removed successfully!", Toast.LENGTH_SHORT).show()
                            tableLayout.removeView(row)
                        } else {
                            Toast.makeText(this@ShowPlayersActivity, "Failed to remove player.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            row.addView(removeButton)

            tableLayout.addView(row)
        }
    }
}
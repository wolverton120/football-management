package com.sqlite.footballmanagement

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class EditPlayerActivity : AppCompatActivity() {

    private var playerId: Int? = null
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_player)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        dbHelper = DatabaseHelper(this)

        val etPlayerName = findViewById<EditText>(R.id.etPlayerName)
        val etPlayerAge = findViewById<EditText>(R.id.etPlayerAge)
        val etPlayerSalary = findViewById<EditText>(R.id.etPlayerSalary)
        val etPlayerGoalsScored = findViewById<EditText>(R.id.etPlayerGoalsScored)
        val etPlayerGoalsAssisted = findViewById<EditText>(R.id.etPlayerGoalsAssisted)
        val etPlayerCleanSheets = findViewById<EditText>(R.id.etPlayerMostCleanSheets)
        val btnSave = findViewById<Button>(R.id.btnEditPlayer)

        playerId = intent.getIntExtra("player_id", -1)
        if (playerId != null && playerId != -1) {
            val player = dbHelper.getPlayerById(playerId!!)
            if (player != null) {
                etPlayerName.setText(player["player_name"])
                etPlayerAge.setText(player["age"])
                etPlayerSalary.setText(String.format(Locale.US, "%.0f", player["salary"]?.toDoubleOrNull() ?: 0.0))
                etPlayerGoalsScored.setText(player["goals_scored"])
                etPlayerGoalsAssisted.setText(player["goals_assisted"])
                etPlayerCleanSheets.setText(player["most_clean_sheets"])
            }
        }

        btnSave.setOnClickListener {
            val playerName = etPlayerName.text.toString()
            val playerAge = etPlayerAge.text.toString().toIntOrNull()
            val playerSalary = etPlayerSalary.text.toString().toDoubleOrNull()
            val playerGoalsScored = etPlayerGoalsScored.text.toString().toIntOrNull()
            val playerGoalsAssisted = etPlayerGoalsAssisted.text.toString().toIntOrNull()
            val playerCleanSheets = etPlayerCleanSheets.text.toString().toIntOrNull()

            if (playerAge != null && playerSalary != null && playerGoalsScored != null &&
                playerGoalsAssisted != null && playerCleanSheets != null) {

                val contentValues = ContentValues().apply {
                    put("player_name", playerName)
                    put("age", playerAge)
                    put("salary", playerSalary)
                    put("goals_scored", playerGoalsScored)
                    put("goals_assisted", playerGoalsAssisted)
                    put("most_clean_sheets", playerCleanSheets)
                }

                val success = dbHelper.editPlayer(playerId!!, contentValues)
                if (success) {
                    Toast.makeText(this, "Player updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update player.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid input.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



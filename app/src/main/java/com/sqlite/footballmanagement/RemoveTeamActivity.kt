// This was originally written when removing player/teams were a separate button functionality.
// But since I've moved it to player/team window these have become redundant. Can't be bothered to remove though who cares
package com.sqlite.footballmanagement

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RemoveTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_team)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val etTeamId = findViewById<EditText>(R.id.etTeamId)
        val btnRemoveTeam = findViewById<Button>(R.id.btnRemoveTeam)

        val db = DatabaseHelper(this)

        btnRemoveTeam.setOnClickListener {
            val teamId = etTeamId.text.toString().toIntOrNull()

            if (teamId != null) {
                val success = db.removeTeam(teamId)
                if (success) {
                    Toast.makeText(this, "Team removed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to remove team. ID not found.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid Team ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

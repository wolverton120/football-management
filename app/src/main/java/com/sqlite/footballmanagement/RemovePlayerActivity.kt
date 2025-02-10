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

class RemovePlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_player)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val etPlayerId = findViewById<EditText>(R.id.etPlayerId)
        val btnRemovePlayer = findViewById<Button>(R.id.btnRemovePlayer)

        val db = DatabaseHelper(this)

        btnRemovePlayer.setOnClickListener {
            val playerId = etPlayerId.text.toString().toIntOrNull()

            if (playerId != null) {
                val success = db.removePlayer(playerId)
                if (success) {
                    Toast.makeText(this, "Player removed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to remove player. ID not found.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid Player ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

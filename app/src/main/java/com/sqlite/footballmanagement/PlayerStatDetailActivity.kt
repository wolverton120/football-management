package com.sqlite.footballmanagement

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PlayerStatDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_stat_detail)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val statsTitle = intent.getStringExtra("STAT_TITLE") ?: "Stat"
        val titleTextView = findViewById<TextView>(R.id.txtStatsTitle)
        titleTextView.text = statsTitle

        val db = DatabaseHelper(this)
        val statsTable = findViewById<TableLayout>(R.id.statsTable)

        val statsData = when (statsTitle) {
            "Goals" -> db.getTopGoalScorers()
            "Assists" -> db.getTopAssisters()
            "Clean Sheets" -> db.getTopGoalkeepers()
            "Salary" -> db.getTopEarners()
            "Age" -> db.getMostAged()
            "Award" -> db.getPlayerAwards()
            else -> emptyList()
        }

        populateStatsTable(statsTable, statsData)
    }

    private fun populateStatsTable(table: TableLayout, stats: List<Map<String, String>>) {
        table.removeAllViews()

        for (player in stats) {
            val row = TableRow(this)
            for (value in player.values) {
                val textView = TextView(this).apply {
                    text = value
                    textSize = 16f
                    setTypeface(null, Typeface.NORMAL)
                    setPadding(16, 16, 16, 16)
                }
                row.addView(textView)
            }
            table.addView(row)
        }
    }
}


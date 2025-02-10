package com.sqlite.footballmanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "FootballDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE Team (
                team_id INTEGER PRIMARY KEY,
                team_name TEXT NOT NULL,
                match_won INTEGER DEFAULT 0,
                match_lost INTEGER DEFAULT 0,
                match_drew INTEGER DEFAULT 0,
                points_earned INTEGER DEFAULT 0,
                goals_scored INTEGER DEFAULT 0,
                goals_conceded INTEGER DEFAULT 0
            )
            """
        )

        db?.execSQL(
            """
            CREATE TABLE Player (
                player_id INTEGER PRIMARY KEY,
                player_name TEXT NOT NULL,
                age INTEGER NOT NULL,
                salary REAL NOT NULL,
                goals_scored INTEGER DEFAULT 0,
                goals_assisted INTEGER DEFAULT 0,
                most_clean_sheets INTEGER DEFAULT 0
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Team")
        db?.execSQL("DROP TABLE IF EXISTS Player")
        onCreate(db)
    }

    private fun getNextId(tableName: String, idColumn: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT MAX($idColumn) AS max_id FROM $tableName", null)
        val nextId = if (cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndexOrThrow("max_id")) != 0) {
            cursor.getInt(cursor.getColumnIndexOrThrow("max_id")) + 1
        } else {
            1
        }
        cursor.close()
        return nextId
    }

    fun getPlayerById(playerId: Int): Map<String, String>? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM player WHERE player_id = ?", arrayOf(playerId.toString()))
        return if (cursor.moveToFirst()) {
            val player = mutableMapOf<String, String>()
            player["player_name"] = cursor.getString(cursor.getColumnIndexOrThrow("player_name"))
            player["age"] = cursor.getString(cursor.getColumnIndexOrThrow("age"))
            player["salary"] = cursor.getString(cursor.getColumnIndexOrThrow("salary"))
            player["goals_scored"] = cursor.getString(cursor.getColumnIndexOrThrow("goals_scored"))
            player["goals_assisted"] = cursor.getString(cursor.getColumnIndexOrThrow("goals_assisted"))
            player["most_clean_sheets"] = cursor.getString(cursor.getColumnIndexOrThrow("most_clean_sheets"))
            cursor.close()
            player
        } else {
            cursor.close()
            null
        }
    }

    fun getTeamById(teamId: Int): Map<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Team WHERE team_id = ?", arrayOf(teamId.toString()))
        return if (cursor.moveToFirst()) {
            val team = mutableMapOf<String, String>()
            team["team_name"] = cursor.getString(cursor.getColumnIndexOrThrow("team_name"))
            team["match_won"] = cursor.getInt(cursor.getColumnIndexOrThrow("match_won")).toString()
            team["match_lost"] = cursor.getInt(cursor.getColumnIndexOrThrow("match_lost")).toString()
            team["match_drew"] = cursor.getInt(cursor.getColumnIndexOrThrow("match_drew")).toString()
            team["points_earned"] = cursor.getInt(cursor.getColumnIndexOrThrow("points_earned")).toString()
            team["goals_scored"] = cursor.getInt(cursor.getColumnIndexOrThrow("goals_scored")).toString()
            team["goals_conceded"] = cursor.getInt(cursor.getColumnIndexOrThrow("goals_conceded")).toString()
            cursor.close()
            team
        } else {
            cursor.close()
            null
        }
    }

    fun getTeamRankings(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT ROW_NUMBER() OVER (ORDER BY points_earned DESC) AS rank,
               team_id,
               team_name,
               points_earned
        FROM Team
        """, null
        )
        val rankings = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                rankings.add(
                    mapOf(
                        "rank" to cursor.getInt(cursor.getColumnIndexOrThrow("rank")).toString(),
                        "team_id" to cursor.getInt(cursor.getColumnIndexOrThrow("team_id")).toString(),
                        "team_name" to cursor.getString(cursor.getColumnIndexOrThrow("team_name")),
                        "points_earned" to cursor.getInt(cursor.getColumnIndexOrThrow("points_earned")).toString()
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return rankings
    }

    fun getGoalDifference(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT team_id, team_name, (goals_scored - goals_conceded) AS goal_difference
        FROM Team
        ORDER BY goal_difference DESC
        """, null
        )
        val differences = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                differences.add(
                    mapOf(
                        "team_id" to cursor.getInt(cursor.getColumnIndexOrThrow("team_id")).toString(),
                        "team_name" to cursor.getString(cursor.getColumnIndexOrThrow("team_name")),
                        "goal_difference" to cursor.getInt(cursor.getColumnIndexOrThrow("goal_difference")).toString()
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return differences
    }

    fun getRelegationTeams(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT 
            team_id, 
            team_name, 
            points_earned, 
            (SELECT COUNT(*) + 1 FROM Team AS t2 WHERE t2.points_earned > t1.points_earned) AS rank,
            'Relegated' AS status
        FROM Team AS t1
        ORDER BY points_earned ASC
        LIMIT 3
        """, null
        )
        val relegated = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                relegated.add(
                    mapOf(
                        "team_id" to cursor.getInt(cursor.getColumnIndexOrThrow("team_id")).toString(),
                        "team_name" to cursor.getString(cursor.getColumnIndexOrThrow("team_name")),
                        "points_earned" to cursor.getInt(cursor.getColumnIndexOrThrow("points_earned")).toString(),
                        "rank" to cursor.getInt(cursor.getColumnIndexOrThrow("rank")).toString(),
                        "status" to cursor.getString(cursor.getColumnIndexOrThrow("status"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return relegated
    }

    fun getTopGoalScorers(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, goals_scored 
        FROM Player 
        WHERE most_clean_sheets = 0 
        ORDER BY goals_scored DESC
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    fun getTopAssisters(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, goals_assisted 
        FROM Player 
        WHERE most_clean_sheets = 0 
        ORDER BY goals_assisted DESC
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    fun getTopGoalkeepers(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, most_clean_sheets 
        FROM Player 
        WHERE goals_scored = 0 AND goals_assisted = 0 
        ORDER BY most_clean_sheets DESC
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    fun getTopEarners(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, salary 
        FROM Player 
        ORDER BY salary DESC
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    fun getMostAged(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, age 
        FROM Player 
        ORDER BY age DESC
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    fun getPlayerAwards(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT player_id, player_name, 'Golden Boot Winner' AS award
        FROM Player
        WHERE goals_scored = (SELECT MAX(goals_scored) FROM Player)
        UNION
        SELECT player_id, player_name, 'Playmaker of the Year Winner' AS award
        FROM Player
        WHERE goals_assisted = (SELECT MAX(goals_assisted) FROM Player)
        UNION
        SELECT player_id, player_name, 'Golden Glove Winner' AS award
        FROM Player
        WHERE most_clean_sheets = (SELECT MAX(most_clean_sheets) FROM Player)
        ORDER BY player_name, award
        """, null
        )
        return generatePlayerStatsList(cursor)
    }

    private fun generatePlayerStatsList(cursor: android.database.Cursor): List<Map<String, String>> {
        val statsList = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val stats = mutableMapOf<String, String>()
                for (i in 0 until cursor.columnCount) {
                    stats[cursor.getColumnName(i)] = cursor.getString(i)
                }
                statsList.add(stats)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return statsList
    }

    fun addTeam(team: ContentValues): Boolean {
        val nextTeamId = getNextId("Team", "team_id")
        team.put("team_id", nextTeamId)
        val db = writableDatabase
        val result = db.insert("Team", null, team)
        return result != -1L
    }

    fun editTeam(teamId: Int, team: ContentValues): Boolean {
        val db = writableDatabase
        val result = db.update("Team", team, "team_id = ?", arrayOf(teamId.toString()))
        return result > 0
    }

    fun removeTeam(teamId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("Team", "team_id = ?", arrayOf(teamId.toString()))
        return result > 0
    }

    fun addPlayer(player: ContentValues): Boolean {
        val nextPlayerId = getNextId("Player", "player_id")
        player.put("player_id", nextPlayerId)  // Set the next available ID
        val db = writableDatabase
        val result = db.insert("Player", null, player)
        return result != -1L
    }

    fun editPlayer(playerId: Int, player: ContentValues): Boolean {
        val db = writableDatabase
        val result = db.update("Player", player, "player_id = ?", arrayOf(playerId.toString()))
        return result > 0
    }

    fun removePlayer(playerId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("Player", "player_id = ?", arrayOf(playerId.toString()))
        return result > 0
    }

    fun getAllTeams(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Team", null)
        val teams = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                teams.add(
                    mapOf(
                        "team_id" to cursor.getInt(cursor.getColumnIndexOrThrow("team_id")).toString(),
                        "team_name" to cursor.getString(cursor.getColumnIndexOrThrow("team_name")),
                        "match_won" to cursor.getInt(cursor.getColumnIndexOrThrow("match_won")).toString(),
                        "match_lost" to cursor.getInt(cursor.getColumnIndexOrThrow("match_lost")).toString(),
                        "match_drew" to cursor.getInt(cursor.getColumnIndexOrThrow("match_drew")).toString(),
                        "points_earned" to cursor.getInt(cursor.getColumnIndexOrThrow("points_earned")).toString(),
                        "goals_scored" to cursor.getInt(cursor.getColumnIndexOrThrow("goals_scored")).toString(),
                        "goals_conceded" to cursor.getInt(cursor.getColumnIndexOrThrow("goals_conceded")).toString()
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return teams
    }

    fun getAllPlayers(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Player", null)
        val players = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                players.add(
                    mapOf(
                        "player_id" to cursor.getInt(cursor.getColumnIndexOrThrow("player_id")).toString(),
                        "player_name" to cursor.getString(cursor.getColumnIndexOrThrow("player_name")),
                        "age" to cursor.getInt(cursor.getColumnIndexOrThrow("age")).toString(),
                        "salary" to cursor.getDouble(cursor.getColumnIndexOrThrow("salary")).toString(),
                        "goals_scored" to cursor.getInt(cursor.getColumnIndexOrThrow("goals_scored")).toString(),
                        "goals_assisted" to cursor.getInt(cursor.getColumnIndexOrThrow("goals_assisted")).toString(),
                        "most_clean_sheets" to cursor.getInt(cursor.getColumnIndexOrThrow("most_clean_sheets")).toString()
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return players
    }
}




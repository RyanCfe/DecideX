package com.example.decidex

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DecisionDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "decidex.db"
        private const val DB_VERSION = 1
        private const val TABLE = "decisions"
        private const val COL_ID = "id"
        private const val COL_OPTIONS = "options"
        private const val COL_RESULT = "result"
        private const val COL_GOAL = "goal"
        private const val COL_ENERGY = "energy"
        private const val COL_URGENCY = "urgency"
        private const val COL_DATE = "date_time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_OPTIONS TEXT,
                $COL_RESULT TEXT,
                $COL_GOAL TEXT,
                $COL_ENERGY TEXT,
                $COL_URGENCY TEXT,
                $COL_DATE DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun insertDecision(options: String, result: String, goal: String, energy: String, urgency: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_OPTIONS, options)
            put(COL_RESULT, result)
            put(COL_GOAL, goal)
            put(COL_ENERGY, energy)
            put(COL_URGENCY, urgency)
        }
        db.insert(TABLE, null, values)
        db.close()
    }

    fun getAllDecisions(): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE ORDER BY $COL_ID DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val result  = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESULT))
                val goal    = cursor.getString(cursor.getColumnIndexOrThrow(COL_GOAL))
                val urgency = cursor.getString(cursor.getColumnIndexOrThrow(COL_URGENCY))
                val date    = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                list.add("✓ $result\nGoal: $goal | Urgency: $urgency\n$date")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun clearAll() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE")
        db.close()
    }
}

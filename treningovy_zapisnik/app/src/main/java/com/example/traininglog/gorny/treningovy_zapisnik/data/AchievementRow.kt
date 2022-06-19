package com.example.traininglog.gorny.treningovy_zapisnik.data


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data trieda ktora reprezentuje achievement v databaze
 */
@Entity (tableName = "achievements")
data class AchievementRow(

    @PrimaryKey (autoGenerate = true)
    val id:Int = 0,

    val logType:String,
    val achType:String,
    val imageOfType: Int,
    val description: String,
    val current: Double,
    val max: Double,
    val completed: Boolean
    )



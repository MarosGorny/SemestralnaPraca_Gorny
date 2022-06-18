package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AchievementRow(

    @PrimaryKey (autoGenerate = true)
    val id:Int,

    val logType:String,
    val achType:String,
    val imageOfType: Int,
    val description: String,
    val current: Double,
    val max: Double,
    val completed: Boolean
    )



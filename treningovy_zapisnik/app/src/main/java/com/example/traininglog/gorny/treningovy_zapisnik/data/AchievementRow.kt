package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.widget.ImageView

data class AchievementRow(
    val id:Int,
    val logType:String,
    val achievementType:String,
    val imageOfType: Int,
    val description: String,
    val current: Double,
    val max: Double,
    val completed: Boolean
    )


{
}
package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.res.Resources
import com.example.traininglog.gorny.treningovy_zapisnik.R

fun runAchievementList(): Array<AchievementRow> {
    return arrayOf(

            AchievementRow(
            id = 1,
            achievementType = "Run",
            imageOfType = R.drawable.run,
            description = "Run total 10 km",
            completed = false
            ),
            AchievementRow(
                id = 2,
                achievementType = "Swim",
                imageOfType = R.drawable.swim,
                description = "Swim total 1 km",
                completed = false
            ),
            AchievementRow(
                id = 3,
                achievementType = "Bike",
                imageOfType = R.drawable.bike,
                description = "Bike total 100 km",
                completed = false
            )
    )
}
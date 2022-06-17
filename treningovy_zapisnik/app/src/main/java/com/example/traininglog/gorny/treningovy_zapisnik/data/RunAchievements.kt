package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.res.Resources
import com.example.traininglog.gorny.treningovy_zapisnik.R

fun runAchievementList(resources: Resources): List<AchievementRow> {
    return listOf(
            AchievementRow(
                id = 1,
                logType =  "Run",
                achievementType = "Distance",
                imageOfType = R.drawable.run,
                description = "Run total 10 km",
                current = 20.0,
                max =  10.0,
                completed = false
            ),
            AchievementRow(
                id = 2,
                logType = "Swim",
                achievementType = "Distance",
                imageOfType = R.drawable.swim,
                description = "Swim total 1 km",
                current = 0.0,
                max = 1000.0,
                completed = false
            ),
            AchievementRow(
                id = 3,
                logType = "Bike",
                achievementType = "Distance",
                imageOfType = R.drawable.bike,
                description = "Bike total 100 km",
                current = 0.0,
                max = 100.0,
                completed = false
            ),
    )

}
package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.res.Resources
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow

fun trainingLogsList(resources: Resources): List<TrainingLogRow> {
    return listOf(
        TrainingLogRow(
            id = 1,
            logTypeTitle = "Swim",
            dateOfLog = "2024 - 11 - 23",
            timeOfLog = "15:23",
            timeTitle = "Time",
            durationOfLog = "00:39:23",
            distanceMetricTitle = "m",
            distance = 500.0,
            fourthColumnTitle = "min/km",
            fourthColumnMinPKm = "4:00"
        ),
        TrainingLogRow(
            id = 2,
            logTypeTitle = "Run",
            dateOfLog = "2022 - 11 - 23",
            timeOfLog = "15:23",
            timeTitle = "Time",
            durationOfLog = "00:24:23",
            distanceMetricTitle = "km",
            distance = 30.3,
            fourthColumnTitle = "km/h",
            fourthColumnMinPKm = "6:00"
        ),
        TrainingLogRow(
            id = 1,
            logTypeTitle = "Bike",
            dateOfLog = "2019 - 11 - 23",
            timeOfLog = "15:23",
            timeTitle = "Time",
            durationOfLog = "00:00:23",
            distanceMetricTitle = "km",
            distance = 23.3,
            fourthColumnTitle = "km/h",
            fourthColumnMinPKm = "8:00"
        ),

    )
}
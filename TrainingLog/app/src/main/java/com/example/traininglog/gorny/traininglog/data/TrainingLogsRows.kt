package com.example.traininglog.gorny.traininglog.data

import android.content.res.Resources
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun trainingLogsList(resources: Resources): List<TrainingLogRow> {
    return listOf(
        TrainingLogRow(
            1,
            "Bike",
            Date(2022,11,23),
            (15.hours + 23.minutes),
            "Time",
            (23.minutes),
            "km",
            23.3,
            "km/h",
            6.minutes
        ),
        TrainingLogRow(
            2,
            "Run",
            Date(2022,2,23),
            (15.hours + 23.minutes),
            "Time",
            (23.minutes),
            "km",
            23.3,
            "km/h",
            6.minutes
        ),
        TrainingLogRow(
            3,
            "Swim",
            Date(2022,11,23),
            (15.hours + 23.minutes),
            "Time",
            (40.minutes),
            "km",
            0.5,
            "km/h",
            10.minutes
        ),

    )
}

/*
            //tu by som mal naplnit arrayList
            val datumAktivityRun:Date = Date(2022, 11, 23)
    val casZaciatkuAktivityRun: kotlin.time.Duration = 15.hours + 23.minutes
    //druhy stlpec
    val casAktivityRun: kotlin.time.Duration = 48.minutes
    //stvrty stlpec
    val casMinPKm: kotlin.time.Duration = 6.minutes + 23.seconds
 */
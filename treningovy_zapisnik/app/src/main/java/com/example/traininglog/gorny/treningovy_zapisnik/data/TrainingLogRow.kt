package com.example.traininglog.gorny.treningovy_zapisnik.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class TrainingLogRow(
    @PrimaryKey
    val id:Long =0L,

    @ColumnInfo(name = "log_type_title")
    val logTypeTitle:String,

    @ColumnInfo(name = "date_of_log")
    val dateOfLog:String,

    @ColumnInfo(name = "time_of_log")
    val timeOfLog:String,

    @ColumnInfo(name = "time_title")
    val timeTitle:String = "Time",

    @ColumnInfo(name = "duration_of_workout")
    val durationOfLog:String,

    @ColumnInfo(name = "distance_title")
    val distanceMetricTitle:String = "unknown",

    @ColumnInfo(name = "distance_of_workout")
    val distance:Double,

    @ColumnInfo(name = "stats_title")
    val fourthColumnTitle:String = "unknown",

    @ColumnInfo(name = "stats_of_workout")
    val fourthColumnMinPKm:String = "unknown"
    )
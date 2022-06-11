package com.example.traininglog.gorny.traininglog.data


import java.util.*

data class TrainingLogRow(
    val id:Long,
    val logTypeTitle:String,
    val dateOfLog:Date,
    val timeOfLog:kotlin.time.Duration,
    val timeTitle:String,
    val durationOfLog:kotlin.time.Duration,
    val thirdColumnTitle:String,
    val thirdColumnStat:Double,
    val fourthColumnTitle:String,
    val fourthColumnMinPKm:kotlin.time.Duration)
{
}
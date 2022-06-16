package com.example.traininglog.gorny.traininglog.data


import java.util.*

data class TrainingLogRow(
    val id:Long,
    val logTypeTitle:String,
    val dateOfLog:String,
    val timeOfLog:String,
    val timeTitle:String = "Time",
    val durationOfLog:String,
    val distanceMetricTitle:String,
    val distance:Double,
    val fourthColumnTitle:String,
    val fourthColumnMinPKm:String)
{
}
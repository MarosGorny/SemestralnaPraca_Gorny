package com.example.traininglog.gorny.traininglog.trainingLogs

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traininglog.gorny.traininglog.data.DataSource
import com.example.traininglog.gorny.traininglog.data.EnumAktivity
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TrainingListViewModel(val dataSource: DataSource) : ViewModel() {

    val trainingLogsLiveData = dataSource.getTrainingLogList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertTrainingLog(logType: String?, dateOfLog: String?,
                          timeOfLog: String?, duration: String?,
                          distance: Double?) {
        //If enum or third column stat is null, return
        if(logType == null)
            return
        //TODO double-distance sa musi zaokruhlit na dve desatinne miesta
        //TODO spravit tak, aby to nahravalo info ktore tam chcem mat
        var formattedTime:String = ""
        var formattedDate:String = ""
        if (timeOfLog == null) {
            val current = LocalDateTime.now()
            val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
            val formatterDate = DateTimeFormatter.ofPattern("dd.MM.YY")
            formattedTime = current.format(formatterTime)
            formattedDate = current.format(formatterDate)
        }


        var newDateOfLog:String = dateOfLog ?: formattedDate
        var newTimeOfLog:String = timeOfLog ?: formattedTime
        var newDuration:String = duration ?: "00:00:00"
        var newDistance:Double = distance ?: 0.0
        lateinit var newStatsTitle:String
        lateinit var newStats:String

        val hours: Int = newDuration.substring(0,2).toInt()
        val minute: Int = newDuration.substring(3,5).toInt()
        val seconds: Int = newDuration.substring(6,8).toInt()

        var newDistanceMetricTitle:String = if(logType == "Run" || logType == "Bike") {
            "km"
        } else {
            "meters"
        }

        if (logType == "Run" ) {
            newStatsTitle = "min/km"
            newStats = calcucalteRunPace(newDistance,newDuration)
        }  else if (logType == "Swim") {
            newStatsTitle = "min/100m"
            newStats = "trebaVypoct"
        } else {
            newStatsTitle = "km/h"
            newStats = ""
        }



        //Adding activity log
        val newTrainingLogRow = TrainingLogRow(
            Random.nextLong(),
            logType,
            newDateOfLog,
            newTimeOfLog,
            timeTitle = "Time",
            newDuration,
            newDistanceMetricTitle,
            newDistance,
            newStatsTitle,
            newStats
        )

        dataSource.addTrainingLog(newTrainingLogRow)

    }

}

class TrainingListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainingListViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun calcucalteRunPace(distaneOfRun:Double, durationOfRun: String): String {
    val seconds = durationOfRun.substring(6,8).toInt()
    val minutes = durationOfRun.substring(3,5).toInt()
    val hours = durationOfRun.substring(0,2).toInt()

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerKm =  totalSeconds/distaneOfRun

    val paceMin = floor ((secPerKm/60)).toInt()
    val paceSecond = ceil ( ((secPerKm)-paceMin)*60).toInt().toString()

    return "$paceMin:$paceSecond"
}
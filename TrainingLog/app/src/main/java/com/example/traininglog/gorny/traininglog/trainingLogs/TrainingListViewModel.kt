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

        var formattedHour:String = ""
        if (timeOfLog == null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH")
            formattedHour = current.format(formatter)
        }


        var newDateOfLog:String = dateOfLog ?: "16.16.1616"
        var newTimeOfLog:String = timeOfLog ?: formattedHour
        var newDuration:String = duration ?: "00:00:00"
        var newDistance:Double = distance ?: 0.0
        lateinit var newStatsTitle:String
        lateinit var newStats:String


        var newDistanceMetricTitle:String = if(logType == "Run" || logType == "Bike") {
            "km"
        } else {
            "meters"
        }

        if (logType == "Run" || logType == "Swim") {
            newStatsTitle = "min/km"
            newStats = "trebaVyoct"
        }  else {
            newStatsTitle = "km/h"
            newStats = "trebaVypoct"
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
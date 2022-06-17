package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traininglog.gorny.treningovy_zapisnik.calculateRunPace
import com.example.traininglog.gorny.treningovy_zapisnik.data.DataSource
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

class TrainingListViewModel(val dataSource: DataSource) : ViewModel() {

    val trainingLogsLiveData = dataSource.getTrainingLogList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertTrainingLog(logType: String?, dateOfLog: String?,
                          timeOfLog: String?, duration: String?,
                          distance: Double?) {
        if(logType == null)
            return
        //TODO double-distance sa musi zaokruhlit na dve desatinne miesta
        var formattedTime:String = ""
        var formattedDate:String = ""
        val current = LocalDateTime.now()
        if (timeOfLog == "null") {
            val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
            formattedTime = current.format(formatterTime)
        }  else {
            formattedTime = timeOfLog!!
        }
        if (dateOfLog == "null") {
            val formatterDate = DateTimeFormatter.ofPattern("dd.MM.YY")
            formattedDate = current.format(formatterDate)
        } else {
            formattedDate = dateOfLog!!
        }


        var newDateOfLog:String = formattedDate
        var newTimeOfLog:String = formattedTime
        var newDuration:String = duration ?: "00:00:00"
        var newDistance:Double = distance ?: 0.0
        lateinit var newStatsTitle:String
        lateinit var newStats:String

        val hours: Int = newDuration.substring(0,2).toInt()
        val minute: Int = newDuration.substring(3,5).toInt()
        val seconds: Int = newDuration.substring(6,8).toInt()


        //TODO prepocita na bike a na swim
        var newDistanceMetricTitle:String = if(logType == "Run" || logType == "Bike") {
            "km"
        } else {
            "meters"
        }

        if (logType == "Run" ) {
            newStatsTitle = "min/km"
            newStats = calculateRunPace(newDistance,newDuration)
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
            timeTitle = "Duration",
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


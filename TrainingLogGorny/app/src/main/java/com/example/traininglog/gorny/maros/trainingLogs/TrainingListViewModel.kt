package com.example.traininglog.gorny.maros.trainingLogs

import androidx.lifecycle.ViewModel
import com.example.traininglog.gorny.maros.data.DataSource
import com.example.traininglog.gorny.maros.data.EnumAktivity
import com.example.traininglog.gorny.maros.data.TrainingLogRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TrainingListViewModel(val dataSource: DataSource) : ViewModel() {

    val trainingLogsLiveData = dataSource.getTrainingLogList()

    fun insertTrainingLog(enumAktivity: EnumAktivity?,dateOfLog:Date?,
                          hourOfAcivity:Int?,minuteOfActivity:Int?,
                          hoursOfActivity:Int,minutesOfActivity:Int,secondsOfActivity:Int,
                          thirdColumnStat:Double?,
                            fourthColumnTitle:String) {
        //If enum or third column stat is null, return
        if(enumAktivity == null || thirdColumnStat == null)
            return

        //Date of activity
        var newDate: Date? = dateOfLog
        if (dateOfLog == null) {
            val localDate = LocalDate.now()
            newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        //Time of activity
        val timeOfActivity: kotlin.time.Duration
        if(hourOfAcivity == null || minuteOfActivity == null) {
            var newHourOfActivity: Int? = hourOfAcivity
            var newMinuteOfActivity: Int? = minuteOfActivity
            val current = LocalDateTime.now()

            val formatterHour = DateTimeFormatter.ofPattern("HH")
            val formatterMinute = DateTimeFormatter.ofPattern("mm")
            val formattedHour = current.format(formatterHour)
            val formattedMinute = current.format(formatterMinute)
            newHourOfActivity = formattedHour.toInt()
            newMinuteOfActivity = formattedMinute.toInt()
            timeOfActivity = newHourOfActivity.hours + newMinuteOfActivity.minutes

        } else {
            timeOfActivity = hourOfAcivity!!.hours + minuteOfActivity!!.minutes
        }
        val idTrainingLogRow:Long = Random.nextLong()
        var logTypeTitle:String = ""
        val timeTitle:String = "Time"

        val activityDuration: kotlin.time.Duration = hoursOfActivity.hours + minutesOfActivity.minutes + secondsOfActivity.seconds
        var thirdColumnTitle:String = "km"


        if(enumAktivity == EnumAktivity.RUN) {
            logTypeTitle = "Run"
        } else if (enumAktivity == EnumAktivity.BIKE) {
            logTypeTitle = "Bike"
        } else if (enumAktivity == EnumAktivity.SWIM) {
            logTypeTitle = "Swim"
            thirdColumnTitle = "meters"
        }

        //Adding activity log
        val newTrainingLogRow = TrainingLogRow(
            idTrainingLogRow,
            logTypeTitle,
            newDate!!,
            timeOfActivity,
            timeTitle,
            activityDuration,
            thirdColumnTitle,
            thirdColumnStat!!,
            "test",
            999.hours
        )

        dataSource.addTrainingLog(newTrainingLogRow)

    }

}
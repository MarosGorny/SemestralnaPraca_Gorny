package com.example.traininglog.gorny.treningovy_zapisnik

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor

fun parseDurationNumbersToString(hours:Int,minutes:Int,seconds:Int):String {
    val numbers = listOf(hours,minutes,seconds)
    return numbers.joinToString(separator = ":")
}

fun parseDurationStringToHashMap(duration  : String) : HashMap<String,Int> {
    val list: List<String> = duration.split(":").toList()

    val hashMap: HashMap<String,Int> = HashMap<String,Int>()
    if(list.size == 3) {
        hashMap["Hour"] = list[0].toInt()
        hashMap["Minute"] = list[1].toInt()
        hashMap["Second"] = list[2].toInt()
    } else {
        hashMap["Hour"] = 0
        hashMap["Minute"] = 0
        hashMap["Second"] = 0
    }


    return hashMap
}

fun calculateRunPace(distanceOfRun:Double, durationOfRun: String): String {
    val hours:Int = parseDurationStringToHashMap(durationOfRun)["Hour"]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfRun)["Minute"]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfRun)["Second"]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerKm =  totalSeconds/distanceOfRun

    val paceMin = floor ((secPerKm/60)).toInt()
    val paceSecond = ceil ( ((secPerKm/60)-paceMin)*60).toInt().toString().take(2)

    return "$paceMin:$paceSecond"
}

fun calculateSwimPace100m(distanceOfSwimInMeters:Double, durationOfSwim: String) :String{
    val hours:Int = parseDurationStringToHashMap(durationOfSwim)["Hour"]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfSwim)["Minute"]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfSwim)["Second"]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerMeter =  totalSeconds/distanceOfSwimInMeters
    val secPer100m = (secPerMeter * 100).toInt()
    return "${secPer100m / 60}:${(secPer100m % 60).toString().take(2)}"

}

fun calculateKilometerPerHour(kilometers:Double, durationOfBike:String): String {
    val hours:Int = parseDurationStringToHashMap(durationOfBike)["Hour"]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfBike)["Minute"]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfBike)["Second"]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    if (totalSeconds == 0)
        return "0.00"

    val kmPerHourString = (kilometers/(totalSeconds/60/60)).toString()
    val kilometresString = kmPerHourString.split(".").first().toString()
    val metresString = kmPerHourString.split(".").last().toString().take(2)

    return "$kilometresString.$metresString"
}


@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate():String {
    var formattedTime:String = ""
    val current = LocalDateTime.now()
    val formatterTime = DateTimeFormatter.ofPattern("dd.MM.y")
    formattedTime = current.format(formatterTime)
    return formattedTime
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTime():String {
    var formattedDate:String = ""
    val current = LocalDateTime.now()
    val formatterDate = DateTimeFormatter.ofPattern("HH:mm")
    formattedDate = current.format(formatterDate)
    return formattedDate
}

fun getTempoPostFix(logType:String):String  {
    when(logType) {
        "Run" -> return "min/km"
        "Bike" -> return "km/h"
        "Swim" -> return "min/100m"
    }
    return "ERROR"
}

fun getPaceOfType(logType: String,distance:Double,duration:String):String {
    when(logType) {
        "Run" -> return calculateRunPace(distance,duration)
        "Bike" -> return calculateKilometerPerHour(distance,duration)
        "Swim" -> return calculateSwimPace100m(distance,duration)
    }
    return "ERROR"
}


package com.example.traininglog.gorny.treningovy_zapisnik

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

fun calcucalteRunPace(distaneOfRun:Double, durationOfRun: String): String {
    val hours:Int = parseDurationStringToHashMap(durationOfRun)["Hour"]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfRun)["Minute"]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfRun)["Second"]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerKm =  totalSeconds/distaneOfRun

    val paceMin = floor ((secPerKm/60)).toInt()
    //TODO Treba zaokruhlit na hor na dve desatinne miesta - paceSecond
    val paceSecond = ceil ( ((secPerKm/60)-paceMin)*60).toInt().toString().take(2)

    return "$paceMin:$paceSecond"
}

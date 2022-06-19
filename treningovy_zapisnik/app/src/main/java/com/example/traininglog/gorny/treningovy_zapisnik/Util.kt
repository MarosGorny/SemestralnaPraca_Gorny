package com.example.traininglog.gorny.treningovy_zapisnik


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor

const val HOUR:String = "Hour"
const val MINUTE:String = "Minute"
const val SECOND:String = "Second"

const val BIKE:String = "Bike"
const val RUN:String = "Run"
const val SWIM:String = "Swim"

/**
 * Vrati string v tvare hh:mm:ss na zaklade zadanej hodiny,minuty,sekundy
 *
 * @param hours pocet hodin - Int
 * @param minutes pocet minut - Int
 * @param seconds pocet sekund - Int
 *
 * @return hh:mm:ss - String
 */
fun parseDurationNumbersToString(hours:Int,minutes:Int,seconds:Int):String {
    val numbers = listOf(hours,minutes,seconds)
    return numbers.joinToString(separator = ":")
}


/**
 * Vrati HashMapu ktora bude obsahovat tri kluce
 * Hour, Minute, Second.
 *
 * @param duration dlzka workoutu v tvare hh:mm:ss - String
 * @return HashMap<String,Int>
 */
fun parseDurationStringToHashMap(duration  : String) : HashMap<String,Int> {
    val list: List<String> = duration.split(":").toList()

    val hashMap: HashMap<String,Int> = HashMap()
    if(list.size == 3) {
        hashMap[HOUR] = list[0].toInt()
        hashMap[MINUTE] = list[1].toInt()
        hashMap[SECOND] = list[2].toInt()
    } else {
        hashMap[HOUR] = 0
        hashMap[MINUTE] = 0
        hashMap[MINUTE] = 0
    }

    return hashMap
}

/**
 * Prepocita tempo v tvare min:sec na kilometer
 *
 * @param distanceOfRun Vzdialenost behu v km - Double
 * @param durationOfRun Cas behania, v tvare hh:mm:ss - String
 *
 * @return min:ss - String
 */
fun calculateRunPace(distanceOfRun:Double, durationOfRun: String): String {
    val hours:Int = parseDurationStringToHashMap(durationOfRun)[HOUR]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfRun)[MINUTE]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfRun)[SECOND]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerKm =  totalSeconds/distanceOfRun

    val paceMin = floor ((secPerKm/60)).toInt()
    val paceSecond = ceil ( ((secPerKm/60)-paceMin)*60).toInt().toString().take(2)

    return "$paceMin:$paceSecond"
}

/**
 * Prepocita tempo v tvare min:sec na 100 metrov
 *
 * @param distanceOfSwimInMeters Vzdialenost plavania v metroch - Double
 * @param durationOfSwim Cas plavania, tvare hh:mm:ss - String
 *
 * @return min:ss ako String
 */
fun calculateSwimPace100m(distanceOfSwimInMeters:Double, durationOfSwim: String) :String{
    val hours:Int = parseDurationStringToHashMap(durationOfSwim)[HOUR]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfSwim)[MINUTE]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfSwim)[SECOND]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    val secPerMeter =  totalSeconds/distanceOfSwimInMeters
    val secPer100m = (secPerMeter * 100).toInt()
    return "${secPer100m / 60}:${(secPer100m % 60).toString().take(2)}"

}

/**
 * Prepocita tempo v tvare km/h
 *
 * @param kilometers Vzdialenost bicyklovania v kilometroch - Double
 * @param durationOfBike Cas bicyklovania v tvare hh:mm:ss- String
 *
 * @return kilometre.metre - String
 */
fun calculateKilometerPerHour(kilometers:Double, durationOfBike:String): String {
    val hours:Int = parseDurationStringToHashMap(durationOfBike)[HOUR]!!
    val minutes:Int = parseDurationStringToHashMap(durationOfBike)[MINUTE]!!
    val seconds:Int = parseDurationStringToHashMap(durationOfBike)[SECOND]!!

    val totalSeconds = seconds + minutes*60 + hours*60*60
    if (totalSeconds == 0)
        return "0.00"

    val kmPerHourString = (kilometers/(totalSeconds/60/60)).toString()
    val kilometresString = kmPerHourString.split(".").first().toString()
    val metresString = kmPerHourString.split(".").last().toString().take(2)

    return "$kilometresString.$metresString"
}

/**
 * Vrati aktualny datum systemu
 *
 * @return tvar dd.MM.y - String
 */
fun getCurrentDate(): String {
    val current = LocalDateTime.now()
    val formatterTime = DateTimeFormatter.ofPattern("dd.MM.y")
    return current.format(formatterTime)
}

/**
 * Vrati aktualny cas systemu
 *
 * @return tvar HH.mm - String
 */
fun getCurrentTime(): String {
    val current = LocalDateTime.now()
    val formatterDate = DateTimeFormatter.ofPattern("HH:mm")
    return current.format(formatterDate)
}

/**
 * Vrati typ tempa na zaklade typu aktivity
 *
 * @param logType typ aktivity - String
 * @return zalezi od aktivity - String
 */
fun getTempoPostFix(logType:String):String  {
    when(logType) {
        RUN -> return "min/km"
        BIKE -> return "km/h"
        SWIM -> return "min/100m"
    }
    return "ERROR"
}

/**
 * Vrati vypocitane tempo podla aktivity
 *
 * @param logType typ aktivity - String
 * @param distance dlzka aktivity - Double
 * @param duration straveny cas aktivitou v tvare hh:mm:ss - String
 * @return zalezi od aktivity - String
 */
fun getPaceOfType(logType: String,distance:Double,duration:String):String {
    when(logType) {
        RUN -> return calculateRunPace(distance,duration)
        BIKE -> return calculateKilometerPerHour(distance,duration)
        SWIM -> return calculateSwimPace100m(distance,duration)
    }
    return "ERROR"
}


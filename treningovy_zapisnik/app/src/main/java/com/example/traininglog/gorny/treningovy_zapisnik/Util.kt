package com.example.traininglog.gorny.treningovy_zapisnik

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
    }
    hashMap["Hour"] = 0
    hashMap["Minute"] = 0
    hashMap["Second"] = 0

    return hashMap
}

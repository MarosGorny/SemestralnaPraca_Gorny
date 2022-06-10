package com.example.traininglog.gorny.maros.data


import java.util.*

data class TrainingLogRow(var logTypeTitle:String, var dateOfLog:Date, var timeOfLog:kotlin.time.Duration,
                          var timeTitle:String,var durationOfLog:kotlin.time.Duration,
                          var thirdColumnTitle:String,var thirdColumnStat:Double,
                          var fourthColumnTitle:String,var fourthColumnMinPKm:kotlin.time.Duration) {
/*    var logTypeTitle:String?=null
    var dateOfLog:Date?=null
    var timeOfLog:kotlin.time.Duration?=null

    var timeTitle:String?=null
    var durationOfLog:kotlin.time.Duration?=null

    var thirdColumnTitle:String?=null
    var thirdColumnStat:Double?=null

    var fourthColumnTitle:String?=null
    var fourthColumnMinPKm:kotlin.time.Duration?=null*/


}
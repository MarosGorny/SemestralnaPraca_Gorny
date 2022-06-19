package com.example.traininglog.gorny.treningovy_zapisnik


import android.app.Application
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementDatabase
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRowDatabase


/**
 * Zakladna trieda na udrziavane stavu aplikacie kde sa vytvoria databazy
 * databazy sa vytvoria len ked su potrebne a nie ked sa aplikacia spusti
 */
class LogListApplication : Application() {
    val database : TrainingLogRowDatabase by lazy { TrainingLogRowDatabase.getDatabase(this) }
    val databaseAchievements : AchievementDatabase by lazy { AchievementDatabase.getDatabase(this) }
}
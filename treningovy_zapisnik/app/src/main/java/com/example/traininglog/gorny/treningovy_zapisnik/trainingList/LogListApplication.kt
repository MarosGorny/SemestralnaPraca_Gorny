package com.example.traininglog.gorny.treningovy_zapisnik.trainingList

import android.app.Application
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRowDatabase

class LogListApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database : TrainingLogRowDatabase by lazy { TrainingLogRowDatabase.getDatabase(this) }
}
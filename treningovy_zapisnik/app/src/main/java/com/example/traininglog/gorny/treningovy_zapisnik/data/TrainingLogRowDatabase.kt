package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [TrainingLogRow::class], version = 6, exportSchema = false)
abstract class TrainingLogRowDatabase: RoomDatabase() {

    abstract fun trainingLogRowDao(): TrainingLogRowDao

    companion object {
        @Volatile
        private var INSTANCE: TrainingLogRowDatabase? = null

        fun getDatabase(context: Context): TrainingLogRowDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainingLogRowDatabase::class.java,
                    "log_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
package com.example.traininglog.gorny.treningovy_zapisnik.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface TrainingLogRowDao {

    @Query("SELECT * from trainingLogRow ORDER BY date_of_log ASC")
    fun getItems(): Flow<List<TrainingLogRow>>

    @Query("SELECT * from trainingLogRow WHERE id = :id")
    fun getItem(id: Long): Flow<TrainingLogRow>

    @Query("SELECT SUM(distance_of_workout) from trainingLogRow WHERE log_type_title = :logType")
    fun getDistance(logType:String): Flow<Double>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: TrainingLogRow)

    @Update
    suspend fun update(log: TrainingLogRow)

    @Delete
    suspend fun delete(log:TrainingLogRow)

    @Query("Delete FROM trainingLogRow")
    suspend fun deleteAllItems()

}
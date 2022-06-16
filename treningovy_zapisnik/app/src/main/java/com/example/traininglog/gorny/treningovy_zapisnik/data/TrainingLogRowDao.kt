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

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: TrainingLogRow)

    @Update
    fun update(log: TrainingLogRow)

    @Delete
    fun delete(log:TrainingLogRow)

}
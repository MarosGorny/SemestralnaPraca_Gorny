package com.example.traininglog.gorny.treningovy_zapisnik.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Pristup k databaze workoutov
 */
@Dao
interface TrainingLogRowDao {

    /**
     * Vytiahne vsetky workouty zoradene podla datumu,casu a typu
     */
    @Query("SELECT * from trainingLogRow ORDER BY date_of_log ASC, time_of_log,log_type_title")
    fun getItems(): Flow<List<TrainingLogRow>>

    /**
     * Vytiahne workout na zaklade ID
     */
    @Query("SELECT * from trainingLogRow WHERE id = :id")
    fun getItem(id: Long): Flow<TrainingLogRow>

    /**
     * Vytiahne celkovu vzdialenost podla typu aktivity
     */
    @Query("SELECT SUM(distance_of_workout) from trainingLogRow WHERE log_type_title = :logType")
    fun getDistance(logType:String): Flow<Double>

    /**
     * Vlozi workout a v pripade konfliktu, konflikt ignoruje a vlozenie sa zrusi
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: TrainingLogRow)

    /**
     * Aktualizuje workout
     */
    @Update
    suspend fun update(log: TrainingLogRow)

    /**
     * Vymaze workout
     */
    @Delete
    suspend fun delete(log:TrainingLogRow)

    /**
     * Vymaze vsetky workouty
     */
    @Query("Delete FROM trainingLogRow")
    suspend fun deleteAllItems()

}
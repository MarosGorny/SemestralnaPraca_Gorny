package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.ClipData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface TrainingLogRowDao {

    @Query("SELECT * from trainingLogRow ORDER BY date_of_log ASC")
    fun getItems(): Flow<List<TrainingLogRow>>

    @Query("SELECT * from trainingLogRow WHERE id = :id")
    fun getItem(id: Long): Flow<TrainingLogRow>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: TrainingLogRow)

    @Update
    fun update(log: TrainingLogRow)

    @Delete
    fun delete(log:TrainingLogRow)

}
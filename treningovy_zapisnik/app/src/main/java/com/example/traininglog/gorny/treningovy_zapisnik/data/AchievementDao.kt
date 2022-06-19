package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.icu.text.UnicodeSet.CASE
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * from achievements ORDER BY completed DESC,current DESC ,max DESC")
    fun getItems(): Flow<List<AchievementRow>>

    @Query("SELECT * from achievements WHERE id = :id")
    fun getItem(id: Int): Flow<AchievementRow>

    @Query("SELECT * from achievements where current >= max")
    fun getCompleted(): Flow<List<AchievementRow>>

    @Query("UPDATE achievements SET current = 0.0")
    suspend fun resetCurrent()

    @Query("UPDATE achievements " +
            "SET current = current + :addedValue " +
            "WHERE achType = :achType " +
            "AND  logType = :logType")
    suspend fun updateCurrent(achType: String,logType:String, addedValue:Double )

    @Query("UPDATE achievements " +
            "SET completed = CASE " +
            "WHEN current >= max THEN 1 ELSE 0 END")
    suspend fun updateCompletedStatus()

    @Query("UPDATE achievements " +
            "SET current = :newDistance " +
            "WHERE logType =:logType ")
    suspend fun setDistanceOfType(logType: String, newDistance:Double)


    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(achievementRow: AchievementRow)

    @Update
    suspend fun update(achievementRow: AchievementRow)

    @Delete
    suspend fun delete(achievementRow: AchievementRow)


}
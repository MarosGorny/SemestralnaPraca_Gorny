package com.example.traininglog.gorny.treningovy_zapisnik.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * from achievementrow ORDER BY max DESC")
    fun getItems(): Flow<List<AchievementRow>>

    @Query("SELECT * from achievementrow WHERE id = :id")
    fun getItem(id: Int): Flow<AchievementRow>

    @Query("SELECT * from achievementrow where current >= max")
    fun getCompleted(): Flow<List<AchievementRow>>

    @Query("UPDATE achievementrow " +
            "SET current = current + :addedValue " +
            "WHERE achType = :achType " +
            "AND  logType = :logType")
    fun updateCurrent(achType: String,logType:String, addedValue:Double )


    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(achievementRow: AchievementRow)

    @Update
    suspend fun update(achievementRow: AchievementRow)

    @Delete
    suspend fun delete(achievementRow: AchievementRow)


}
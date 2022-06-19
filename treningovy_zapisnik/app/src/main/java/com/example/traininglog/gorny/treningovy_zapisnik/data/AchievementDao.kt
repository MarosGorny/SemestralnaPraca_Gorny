package com.example.traininglog.gorny.treningovy_zapisnik.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Pristup k databaze workoutov
 */

@Dao
interface AchievementDao {

    /**
     * Vytiahne vsetky achievementy zoradene podla toho ci boli splnene, aktualnej hodnoty, maximalnej hodnoty
     */
    @Query("SELECT * from achievements ORDER BY completed DESC,current DESC ,max DESC")
    fun getItems(): Flow<List<AchievementRow>>

    /**
     * Vytiahne achievement podla ID
     */
    @Query("SELECT * from achievements WHERE id = :id")
    fun getItem(id: Int): Flow<AchievementRow>

    /**
     * Vytiahne splnene achievementy
     */
    @Query("SELECT * from achievements where current >= max")
    fun getCompleted(): Flow<List<AchievementRow>>

    /**
     * Vyresetuje hodnotu achievementov
     */
    @Query("UPDATE achievements SET current = 0.0")
    suspend fun resetCurrent()

    /**
     * Pripocita vzdialenost do achievementu na zakladate typu achievementu a typu aktivity
     */
    @Query("UPDATE achievements " +
            "SET current = current + :addedValue " +
            "WHERE achType = :achType " +
            "AND  logType = :logType")
    suspend fun updateCurrent(achType: String,logType:String, addedValue:Double )

    /**
     * Aktualizuje ci boli achievmenty splnene alebo nie
     */
    @Query("UPDATE achievements " +
            "SET completed = CASE " +
            "WHEN current >= max THEN 1 ELSE 0 END")
    suspend fun updateCompletedStatus()

    /**
     * Nastavi vzdielnost na zaklade typu
     */
    @Query("UPDATE achievements " +
            "SET current = :newDistance " +
            "WHERE logType =:logType ")
    suspend fun setDistanceOfType(logType: String, newDistance:Double)

    /**
     * Spocita pocet splnenych achievemntov
     */
    @Query("SELECT SUM(completed) from achievements")
    fun sumCompletedAchievement(): Flow<Int>


    /**
     * Vlozi achievement a v pripade konfliktu, konflikt ignoruje a vlozenie sa zrusi
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(achievementRow: AchievementRow)

    /**
     * Aktualizuje achievement
     */
    @Update
    suspend fun update(achievementRow: AchievementRow)

    /**
     * Vymaze achievement
     */
    @Delete
    suspend fun delete(achievementRow: AchievementRow)


}
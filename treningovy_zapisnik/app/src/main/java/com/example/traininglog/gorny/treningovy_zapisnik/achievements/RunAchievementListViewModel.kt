package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import androidx.lifecycle.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.*

/**
 * View Model ktory uchovava odkaz na ulozisko vsetkych achievementov
 */
class RunAchievementListViewModel(achievementDao: AchievementDao) : ViewModel() {

    //Cache all items from the database using LiveData
    val allAchievements: LiveData<List<AchievementRow>> = achievementDao.getItems().asLiveData()

}
/**
 * Factory class pre vytvorenie ViewModel instancie
 */

class RunAchievementListViewModelFactory(private val achievementDao: AchievementDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunAchievementListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunAchievementListViewModel(
                achievementDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
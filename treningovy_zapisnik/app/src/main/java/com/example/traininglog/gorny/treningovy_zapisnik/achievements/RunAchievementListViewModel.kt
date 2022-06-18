package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import androidx.lifecycle.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.*
import kotlinx.coroutines.launch

class RunAchievementListViewModel(private val achievementDao: AchievementDao) : ViewModel() {

    //Cache all items from the database using LiveData
    val allAchievements: LiveData<List<AchievementRow>> = achievementDao.getItems().asLiveData()

    fun updateAchievementRow (
        id:Int,
        logType:String,
        achType:String,
        imageOfType:Int,
        description: String,
        current:Double,
        max:Double,
        completed:Boolean
    ) {
        val updatedAchRow = getUpdatedItemEntry(id, logType,achType,imageOfType,description,current,max,completed)
        updateAchievementRow(updatedAchRow)
    }

    fun addDistance(achievementType: String, logType: String, distanceToAdd:Double) {
        viewModelScope.launch {
            achievementDao.updateCurrent(achievementType,logType, distanceToAdd)
        }
    }

    fun updateAchievementRow(achievementRow: AchievementRow) {
        viewModelScope.launch {
            achievementDao.update(achievementRow)
        }
    }

    /**
     * Called to update an existing entry in the TrainingLog database.
     * Returns an instance of the TrainingLogRow entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        id:Int,
        logType:String,
        achType:String,
        imageOfType:Int,
        description: String,
        current:Double,
        max:Double,
        completed:Boolean
    ): AchievementRow {
        return AchievementRow(
            id = id,
            logType = logType,
            achType = achType,
            imageOfType = imageOfType,
            description = description,
            current = current,
            max = max,
            completed = completed)
    }

    fun retrieveItem(id:Int): LiveData<AchievementRow> {
        return achievementDao.getItem(id).asLiveData()
    }


    private fun insertItem(achievementRow: AchievementRow) {
        viewModelScope.launch {
            achievementDao.insert(achievementRow)
        }
    }

}

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
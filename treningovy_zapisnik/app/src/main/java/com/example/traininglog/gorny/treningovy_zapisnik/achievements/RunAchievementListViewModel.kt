package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.content.Context
import androidx.lifecycle.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.DataSource
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRowDao
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import kotlinx.coroutines.launch

class RunAchievementListViewModel(val dataSource: DataSource) : ViewModel() {

    val runAchievemetsLiveData = dataSource.getRunAchievementList()
    var runTotalDistanceLiveData = dataSource.getDistanceOfRunning()



}

class RunAchievementListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunAchievementListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunAchievementListViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
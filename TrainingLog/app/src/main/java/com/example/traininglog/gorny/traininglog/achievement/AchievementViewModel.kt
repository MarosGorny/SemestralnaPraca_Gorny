package com.example.traininglog.gorny.traininglog.achievement

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traininglog.gorny.traininglog.data.DataSource


class AchievementViewModel(private val dataSource: DataSource) : ViewModel() {

}

class AchievementViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AchievementViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}
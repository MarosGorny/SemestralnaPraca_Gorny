package com.example.traininglog.gorny.traininglog.trainingLogDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traininglog.gorny.traininglog.data.DataSource
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow

class TrainingLogDetailViewModel(private val dataSource: DataSource) : ViewModel() {

    /* Quieries datasource to returns a training log that corresponds to an di. */
    fun getTrainingLogForId(id:Long) : TrainingLogRow? {
        return dataSource.getTrainingLogId(id)
    }

    /* Queries datasource to remove a training log. */
    fun removeTrainingLog(trainingLogRow: TrainingLogRow) {
        dataSource.removeTrainingLog(trainingLogRow)
    }
}

class TrainingLogDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingLogDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainingLogDetailViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}
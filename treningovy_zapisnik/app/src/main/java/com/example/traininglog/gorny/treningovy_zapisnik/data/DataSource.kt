package com.example.traininglog.gorny.treningovy_zapisnik.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource(resources: Resources) {
    private val initialTrainingLogList = trainingLogsList(resources)
    private val trainingLogsLiveData = MutableLiveData(initialTrainingLogList)

    /* Adds training log to liveData and posts value. */
    fun addTrainingLog(trainingLog : TrainingLogRow) {
        val currentList = trainingLogsLiveData.value
        if(currentList == null) {
            //Posts a task to a main thread to set the given value.
            trainingLogsLiveData.postValue(listOf(trainingLog))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0,trainingLog)
            //Posts a task to a main thread to set the given value.
            trainingLogsLiveData.postValue(updatedList)
        }
    }

    /*Removes training log from liveData and post value. */
    fun removeTrainingLog(trainingLog: TrainingLogRow) {
        val currentList = trainingLogsLiveData.value
        if(currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(trainingLog)
            //Posts a task to a main thread to set the given value.
            trainingLogsLiveData.postValue(updatedList)
        }
    }

    /* Returns training log given an ID. */
    fun getTrainingLogId(id: Long): TrainingLogRow? {

        trainingLogsLiveData.value?.let {  trainingLogs ->
            //Returns the first element matching the given predicate, or null if element was not found.
            return trainingLogs.firstOrNull{it.id == id}
        }
        return null
    }

    fun getTrainingLogList(): LiveData<List<TrainingLogRow>> {
        return trainingLogsLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
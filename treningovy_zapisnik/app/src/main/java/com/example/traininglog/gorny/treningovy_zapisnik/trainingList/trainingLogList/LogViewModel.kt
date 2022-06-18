package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.traininglog.gorny.treningovy_zapisnik.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.DataSource
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRowDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 */
class LogViewModel(private val trainingLogRowDao: TrainingLogRowDao) : ViewModel() {


    // Cache all items form the database using LiveData.
    val allTrainingLogs: LiveData<List<TrainingLogRow>> = trainingLogRowDao.getItems().asLiveData()

    /**
     * Returns true if distance is empty, false otherwise.
     */
    fun isDistanceEmpty(trainingLogRow: TrainingLogRow): Boolean {
        return trainingLogRow.distance.isNaN()
    }


    /**
     * Updates an existing TrainingLogRow in the database.
     */
    fun updateTrainingLogRow(
        id:Long,
        logTypeTitle:String,
        dateOfLog:String,
        timeOfLog:String,
        durationOfLog:String,
        distanceMetricTitle:String,
        distance:Double,
        fourthColumnTitle:String,
        fourthColumnMinPKm:String
    ) {
        val updatedTrainingLogRow = getUpdatedItemEntry(id,logTypeTitle,dateOfLog,timeOfLog,durationOfLog,distanceMetricTitle,
                                                        distance,fourthColumnTitle,fourthColumnMinPKm)
        updateTrainingLogRow(updatedTrainingLogRow)
    }

    /**
     * Launching a new coroutine to update an trainingLogRow in a non-blocking way
     */
    private fun updateTrainingLogRow(trainingLogRow: TrainingLogRow) {
        viewModelScope.launch {
            trainingLogRowDao.update(trainingLogRow)
        }
    }

    /**
     * Inserts the new TrainingLogRow into database.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewTrainingLogRow(
                                     logTypeTitle:String,
                                     dateOfLog:String,
                                     timeOfLog:String,
                                     durationOfLog:String,
                                     distance:Double
                            ) {
        val newTrainingLogRow = getNewTrainingLogEntry(logTypeTitle,dateOfLog,timeOfLog,durationOfLog,
            distance)
        insertItem(newTrainingLogRow)
        //dataSource.addDistance(newTrainingLogRow.logTypeTitle,distance)
    }

    /**
     * Launching a new coroutine to insert a trainingLogRow in a non-blocking way
     */
    private fun insertItem(trainingLogRow: TrainingLogRow) {
        viewModelScope.launch {
            trainingLogRowDao.insert(trainingLogRow)
        }
      //  dataSource.addDistance(trainingLogRow.logTypeTitle,trainingLogRow.distance)
    }

    /**
     * Launching a new coroutine to delete a trainingLogRow in a non-blocking way
     */
    fun deleteItem(trainingLogRow: TrainingLogRow) {
        viewModelScope.launch {
            trainingLogRowDao.delete(trainingLogRow)
        }
    }

    /**
     * Launching a new coroutine to delete all trainingLogsRows in a non-blocking way
     */
    fun deleteAllItems() {
        viewModelScope.launch {
            trainingLogRowDao.deleteAllItems()
        }
    }


    /**
     * Retrieve a trainingLogRow from the repository.
     */
    fun retrieveItem(id: Long): LiveData<TrainingLogRow> {
        return trainingLogRowDao.getItem(id).asLiveData()
    }

    fun getDistance(logType: String): LiveData<Double> {

            return trainingLogRowDao.getDistance("Run").asLiveData()
    }

    /**
     * Returns true if the Distance EditText is not empty
     */
    fun isEntryValid(distance: String): Boolean {
        if (distance.isBlank() ) {
            return false
        }
        if(distance.toDouble() == 0.0)
            return false
        return true
    }

    /**
     * Returns an instance of the TrainingLogRow entity class with the item info entered by the user.
     * This will be used to add a new entry to the TrainingLogRow database.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNewTrainingLogEntry(logType: String, date: String, time: String, duration: String, distance:Double): TrainingLogRow {
        var fourthColumnTitle:String = "empty"
        var fourthColumnStats:String  = "empty"
        var newDate:String = date
        var newTime:String = time
        if (date.uppercase() == "SELECT DATE")
            newDate = getCurrentDate()
        if (time.uppercase() == "SELECT TIME")
            newTime = getCurrentTime()

        when(logType) {
            "Run" -> {
                fourthColumnTitle = "min/km"
                fourthColumnStats = calculateRunPace(distance,duration)
            }
            "Bike" -> {
                fourthColumnTitle = "km/h"
                fourthColumnStats = calculateKilometerPerHour(distance,duration)
            }
            "Swim" -> {
                fourthColumnTitle = "min/100m"
                fourthColumnStats = calculateSwimPace100m(distance,duration)
            }
        }


        return TrainingLogRow(
            logTypeTitle = logType,
            dateOfLog = newDate,
            timeOfLog = newTime,
            durationOfLog = duration,
            distance = distance,
            fourthColumnTitle = fourthColumnTitle,
            fourthColumnMinPKm = fourthColumnStats
        )
    }


    /**
     * Called to update an existing entry in the TrainingLog database.
     * Returns an instance of the TrainingLogRow entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        trainingLogId:Long,
        trainingLogLogTypeTitle:String,
        trainingLogDateOfLog:String,
        trainingLogTimeOfLog:String,
        trainingLogDurationOfLog:String,
        trainingLogDistanceMetricTitle:String,
        trainingLogDistance:Double,
        trainingLogFourthColumnTitle:String,
        trainingLogFourthColumnMinPKm:String
    ): TrainingLogRow {
        return TrainingLogRow(
            id = trainingLogId,
            logTypeTitle = trainingLogLogTypeTitle,
            dateOfLog = trainingLogDateOfLog,
            timeOfLog = trainingLogTimeOfLog,
            timeTitle = "Time",
            durationOfLog = trainingLogDurationOfLog,
            distanceMetricTitle = trainingLogDistanceMetricTitle,
            distance = trainingLogDistance,
            fourthColumnTitle = trainingLogFourthColumnTitle,
            fourthColumnMinPKm = trainingLogFourthColumnMinPKm)
    }
}

/**
 * Factory class to instantiate the ViewModel instance.
 */
class LogViewModelFactory(private val trainingLogRowDao: TrainingLogRowDao,private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogViewModel(
                trainingLogRowDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
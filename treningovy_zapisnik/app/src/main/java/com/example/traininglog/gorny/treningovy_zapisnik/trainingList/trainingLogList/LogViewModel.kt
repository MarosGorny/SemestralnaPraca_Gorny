package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import androidx.lifecycle.*
import com.example.traininglog.gorny.treningovy_zapisnik.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementDao
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRowDao
import kotlinx.coroutines.launch



/**
 * View Model ktory uchovava odkaz na ulozisko vsetkych workoutov
 */
class LogViewModel(private val trainingLogRowDao: TrainingLogRowDao, private val achievementDao: AchievementDao) : ViewModel() {


    // Cache all items form the database using LiveData.
    val allTrainingLogs: LiveData<List<TrainingLogRow>> = trainingLogRowDao.getItems().asLiveData()


    /**
     * Aktualizuje existujuci workout v databaze
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
     * Spusti korutinu na aktializaciuje workoutu bez blokovania
     * @param trainingLogRow workout - TrainingLogRow
     */
    private fun updateTrainingLogRow(trainingLogRow: TrainingLogRow) {
        viewModelScope.launch {
            trainingLogRowDao.update(trainingLogRow)
        }
    }

    /**
     * Vlozi novy workout do databazy
     */
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
    }

    /**
     * Spusti korutinu na vlozenie workoutu bez blokovania
     *
     * @param trainingLogRow workout - TrainingLogRow
     */
    private fun insertItem(trainingLogRow: TrainingLogRow) {
        val logType:String = trainingLogRow.logTypeTitle
        val distance:Double = trainingLogRow.distance
        viewModelScope.launch {
            trainingLogRowDao.insert(trainingLogRow)
            achievementDao.updateCurrent("Duration",logType,distance)
            achievementDao.updateCompletedStatus()
        }

    }

    /**
     * Spusti korutinu na vymazanie workoutu bez blokovania
     *
     * @param trainingLogRow workout - TrainingLogRow
     */
    fun deleteItem(trainingLogRow: TrainingLogRow) {
        val logType:String = trainingLogRow.logTypeTitle
        val distance:Double = -trainingLogRow.distance
        viewModelScope.launch {
            trainingLogRowDao.delete(trainingLogRow)
            achievementDao.updateCurrent("Duration",logType,distance)
            achievementDao.updateCompletedStatus()
        }
    }

    /**
     * Spusti korutinu na vymazanie vsetkych workoutov bez blokovania
     */
    fun deleteAllItems() {
        viewModelScope.launch {
            trainingLogRowDao.deleteAllItems()
            achievementDao.resetCurrent()
        }
    }


    /**
     * Ziskanie workoutu z uloziska
     *
     * @param id id workoutu - Long
     *
     * @return LiveData<TrainingLogRow>
     */
    fun retrieveItem(id: Long): LiveData<TrainingLogRow> {
        return trainingLogRowDao.getItem(id).asLiveData()
    }


    /**
     * Ziskanie vzdialenosti workoutu podla typu z uloziska
     *
     * @param logType typ workoutu - String
     *
     * @return LiveData<Double>
     */
    fun getDistance(logType: String): LiveData<Double> {
            return trainingLogRowDao.getDistance(logType).asLiveData()
    }

    fun setAchievementDistance(logType: String, newDistance:Double) {
        viewModelScope.launch {
            achievementDao.setDistanceOfType(logType,newDistance)
            achievementDao.updateCompletedStatus()
        }
    }

    /**
     * Vrati true ak vzdialenost v EditTexte nie je prazdna alebo nulova
     *
     * @param distance vzdialenost workout v tvar hh:mm:ss - String
     * @return TRUE/FALSE - Boolean
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
     *
     * Vrati objekt workoutu ktory ma informacie ktore zadal pouzivatel
     * Tato metoda bude pouzita na vlozenie workoutu do databazy
     *
     * @return novy workout - TraningLogRow
     */
    private fun getNewTrainingLogEntry(logType: String, date: String, time: String, duration: String, distance:Double): TrainingLogRow {
        var fourthColumnTitle = "empty"
        var fourthColumnStats = "empty"
        var newDate:String = date
        var newTime:String = time
        if (date.uppercase() == "SELECT DATE")
            newDate = getCurrentDate()
        if (time.uppercase() == "SELECT TIME")
            newTime = getCurrentTime()

        when(logType) {
            RUN -> {
                fourthColumnTitle = getTempoPostFix(logType)
                fourthColumnStats = calculateRunPace(distance,duration)
            }
            BIKE -> {
                fourthColumnTitle = getTempoPostFix(logType)
                fourthColumnStats = calculateKilometerPerHour(distance,duration)
            }
            SWIM -> {
                fourthColumnTitle = getTempoPostFix(logType)
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
     * Aktualizuje existujuci workout v databaze na zaklade zadanych parametrov
     * a vrati novy objekt workoutu s aktualizovanymi datami
     *
     * @return aktualizovany workout - TrainingLogRow
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
 * Factory class pre vytvorenie ViewModel instancie
 */
class LogViewModelFactory(private val trainingLogRowDao: TrainingLogRowDao, private val achievementDao: AchievementDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogViewModel(
                trainingLogRowDao,
                achievementDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.traininglog.gorny.traininglog.trainingLogDetail

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.traininglog.gorny.traininglog.R
import com.example.traininglog.gorny.traininglog.TRAINING_LOG_ID

class TrainingLogDetailActivity: AppCompatActivity() {
    private val trainingLogDetailViewModel by viewModels<TrainingLogDetailViewModel> {
        TrainingLogDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_training_log_detail)

        var currentTrainingLogId: Long? = null

        /* Connect variables to UI elements. */
        val typeActivity: TextView = findViewById(R.id.title_detail)
        val timeActivity: TextView = findViewById(R.id.time_detail)
        val distanceActivity: TextView = findViewById(R.id.distance_detail)
        val removeTrainingButton: Button = findViewById(R.id.removeButton)

        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            currentTrainingLogId = bundle.getLong(TRAINING_LOG_ID)
        }

        /*If currentTrainingLogId is not null, get corresponding trainingLogDetail and set
        title, time and distance*/
        currentTrainingLogId?.let {
            val currentTrainingLog = trainingLogDetailViewModel.getTrainingLogForId(it)
            typeActivity.text = currentTrainingLog?.logTypeTitle
            timeActivity.text = currentTrainingLog?.durationOfLog
            distanceActivity.text = currentTrainingLog?.distance.toString()

            removeTrainingButton.setOnClickListener {
                if (currentTrainingLog != null) {
                    trainingLogDetailViewModel.removeTrainingLog(currentTrainingLog)
                }
                finish()
            }
        }
    }
}
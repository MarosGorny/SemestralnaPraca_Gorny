package com.example.traininglog.gorny.traininglog

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.traininglog.achievement.AchievementActivity
import com.example.traininglog.gorny.traininglog.addActivity.*
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow
import com.example.traininglog.gorny.traininglog.trainingLogs.HeaderAdapter
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingListViewModel
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingListViewModelFactory
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingLogsRowsAdapter
import com.example.traininglog.gorny.traininglog.trainingLogDetail.TrainingLogDetailActivity

const val TRAINING_LOG_ID = "trainingLog id"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val newTrainingLogActivityRequestCode = 1

    private val trainingLogListViewModel by viewModels<TrainingListViewModel> {
        TrainingListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Instantiates headerAdapter and trainingLogsRowsAdapter. Both adapters are added to concatAdapter.
            which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val trainingLogsRowsAdapter = TrainingLogsRowsAdapter { trainingLogRow -> adapterOnClick(trainingLogRow) }
        val concatAdapter = ConcatAdapter(headerAdapter, trainingLogsRowsAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.trainingLog_list)
        recyclerView.adapter = concatAdapter

        //lambda
        trainingLogListViewModel.trainingLogsLiveData.observe(this, {
            it?.let {
                trainingLogsRowsAdapter.submitList(it as MutableList<TrainingLogRow>)
                headerAdapter.updateTrainingLogCount(it.size)
            }
        })

        val fabAdd: View = findViewById(R.id.floatingButtonAdd)
        fabAdd.setOnClickListener {
            floatingButtonAddOnClick()
        }

        val fabAchievement: View = findViewById(R.id.floatingButtonAchievement)
        fabAchievement.setOnClickListener {
            floatingButtonAchievementOnClick()
        }

    }



    /* Opens TrainingLogDetailActivity when TrainingList item is clicked. */
    private fun adapterOnClick(trainingLogRow: TrainingLogRow) {
        val intent = Intent(this, TrainingLogDetailActivity()::class.java)
        intent.putExtra(TRAINING_LOG_ID, trainingLogRow.id)
        startActivity(intent)
    }

    /* Adds trainingLog to trainingLogList when FAB is clicked. */
    private fun floatingButtonAddOnClick() {
        //intent is to request another activity to achieve a task.
        val intent = Intent(this, AddActivityLog::class.java)
        startActivityForResult(intent, newTrainingLogActivityRequestCode)
    }

    private fun floatingButtonAchievementOnClick() {
        //intent is to request another activity to achieve a task.
        val intent = Intent(this, AchievementActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts trainingLog into viewModel. */
        if (requestCode == newTrainingLogActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->

                val logType = data.getStringExtra(LOG_ACTIVITY)
                val logDate = data.getStringExtra(TRAINING_LOG_DATE)
                val logTime = data.getStringExtra(TRAINING_LOG_TIME)
                val logDistance = data.getStringExtra(TRAINING_LOG_DISTANCE)
                val logDuration = data.getStringExtra(TRAINING_LOG_DURATION)

                trainingLogListViewModel.insertTrainingLog(logType,logDate  ,logTime, logDuration,
                    logDistance?.toDouble()
                )
            }
        }
    }



}
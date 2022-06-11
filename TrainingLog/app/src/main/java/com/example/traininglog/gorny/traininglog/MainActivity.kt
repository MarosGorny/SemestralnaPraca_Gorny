package com.example.traininglog.gorny.traininglog

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.traininglog.data.EnumAktivity
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow
import com.example.traininglog.gorny.traininglog.trainingLogs.HeaderAdapter
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingListViewModel
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingListViewModelFactory
import com.example.traininglog.gorny.traininglog.trainingLogs.TrainingLogsRowsAdapter
import com.example.traininglog.gorny.traininglog.addActivity.AddActivityLog
import com.example.traininglog.gorny.traininglog.addActivity.TRAINING_LOG_DISTANCE
import java.util.*

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

        val fab: View = findViewById(R.id.floatingButtonAdd)
        fab.setOnClickListener {
            floatingButtonAddOnClick()
        }

    }

    /* Opens TrainingLogDetailActivity when TrainingList item is clicked. */
    private fun adapterOnClick(trainingLogRow: TrainingLogRow) {
        //val intent = Intent(this, FlowerDetailActivity()::class.java)
        //intent.putExtra(FLOWER_ID, flower.id)
        //startActivity(intent)
    }

    /* Adds trainingLog to trainingLogList when FAB is clicked. */
    private fun floatingButtonAddOnClick() {
        val intent = Intent(this, AddActivityLog::class.java)
        startActivityForResult(intent, newTrainingLogActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts flower into viewModel. */
        if (requestCode == newTrainingLogActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val logDistance = data.getStringExtra(TRAINING_LOG_DISTANCE)
                //val something = data.getStringExtra(R.id.date_activity_title.toString())
                val doubleSomething = data.getDoubleExtra(R.id.distance_input_number.toString(),0.0)

                //TU MUSIM POVYBERAT VECI Z toho co stlacim
                trainingLogListViewModel.insertTrainingLog(
                    EnumAktivity.RUN,Date(22,1,2022),15,23,
                    3,2,15,doubleSomething,"Test")
            }
        }
    }



}
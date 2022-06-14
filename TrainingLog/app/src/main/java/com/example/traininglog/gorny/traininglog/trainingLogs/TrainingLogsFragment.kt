package com.example.traininglog.gorny.traininglog.trainingLogs

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.traininglog.R
import com.example.traininglog.gorny.traininglog.TRAINING_LOG_ID
import com.example.traininglog.gorny.traininglog.addActivity.*
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow
import com.example.traininglog.gorny.traininglog.trainingLogDetail.TrainingLogDetailActivity

class TrainingLogsFragment : Fragment() {

    private val newTrainingLogActivityRequestCode = 1
    private val viewModel: TrainingListViewModel by activityViewModels()
    private val trainingLogListViewModel by viewModels<TrainingListViewModel> {
        TrainingListViewModelFactory(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_traininglog_list, container, false)

        /* Instantiates headerAdapter and trainingLogsRowsAdapter. Both adapters are added to concatAdapter.
            which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val trainingLogsRowsAdapter = TrainingLogsRowsAdapter { trainingLogRow -> adapterOnClick(trainingLogRow) }
        val concatAdapter = ConcatAdapter(headerAdapter, trainingLogsRowsAdapter)

        val recyclerView: RecyclerView = view.findViewById(R.id.trainingLog_list)
        recyclerView.adapter = concatAdapter

        //lambda
        trainingLogListViewModel.trainingLogsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                trainingLogsRowsAdapter.submitList(it as MutableList<TrainingLogRow>)
                headerAdapter.updateTrainingLogCount(it.size)
            }
        })

        val fabAdd: View = view.findViewById(R.id.floatingButtonAdd)
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_trainingLogListFragment_to_addTrainingLogFragment)
        }

        return view
    }


    /* Opens TrainingLogDetailActivity when TrainingList item is clicked. */
    private fun adapterOnClick(trainingLogRow: TrainingLogRow) {
        findNavController().navigate(R.id.action_trainingLogListFragment_to_logItemFragment)
    }

    /* Adds trainingLog to trainingLogList when FAB is clicked. */
    private fun floatingButtonAddOnClick() {

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
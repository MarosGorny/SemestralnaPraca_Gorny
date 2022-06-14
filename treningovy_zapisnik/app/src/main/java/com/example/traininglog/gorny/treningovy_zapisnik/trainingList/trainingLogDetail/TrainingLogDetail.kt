package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModelFactory

class TrainingLogDetail : Fragment() {



    private val trainingLogListViewModel: TrainingListViewModel by activityViewModels<TrainingListViewModel> {
        TrainingListViewModelFactory(requireContext())
    }



    private val logDetailViewModel: TrainingLogDetailViewModel by activityViewModels<TrainingLogDetailViewModel> {
        TrainingLogDetailViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_training_log_detail, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentTrainingLogId: Long? = null

        /* Connect variables to UI elements. */
        val typeActivity: TextView = view.findViewById(R.id.title_detail)
        val timeActivity: TextView = view.findViewById(R.id.time_detail)
        val distanceActivity: TextView = view.findViewById(R.id.distance_detail)
        val removeTrainingButton: Button = view.findViewById(R.id.removeButton)
        Log.i("TrainingLogDetail","onviewcreated")

        /*Retrieve Bundle*/
        currentTrainingLogId = arguments?.getLong("TRAINING_LOG_ID")


        /*If currentTrainingLogId is not null, get corresponding trainingLogDetail and set
            title, time and distance*/
        currentTrainingLogId?.let {
            Log.i("TrainingLogDetail","LAMBDA")
            val currentTrainingLog = logDetailViewModel.getTrainingLogForId(it)
            typeActivity.text = currentTrainingLog?.logTypeTitle
            timeActivity.text = currentTrainingLog?.durationOfLog
            distanceActivity.text = currentTrainingLog?.distance.toString()

            removeTrainingButton.setOnClickListener {
                Log.i("TrainingLogDetail","Listener")
                if (currentTrainingLog != null) {
                    logDetailViewModel.removeTrainingLog(currentTrainingLog)
                    findNavController().navigate(R.id.action_trainingLogItem_to_trainingLogList)
                }
            }
        }
    }
}
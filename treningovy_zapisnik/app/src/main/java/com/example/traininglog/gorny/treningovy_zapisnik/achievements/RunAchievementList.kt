package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.runAchievementList
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunAchievementList : Fragment() {

    private var _totalDistance: Double = 0.0

    private val runAchievementViewModel by viewModels<RunAchievementListViewModel> {
        RunAchievementListViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            requireContext())
    }

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            requireContext()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_run_achievement, container, false)

        val runAchievementsAdapter = RunAchievementAdapter { runAchievementList -> adapterOnClick(runAchievementList)}

        val recyclerView: RecyclerView = view.findViewById(R.id.achievement_list)
        recyclerView.adapter = runAchievementsAdapter
        Log.i("DISTANCE","OncReateView")
        var something:String = ""

        runAchievementViewModel.runTotalDistanceLiveData.observe(viewLifecycleOwner) { totalDistance ->
            something = totalDistance.toString()
            Log.i("DISTANCE", "insideeeeeee")
            Log.i("DISTANCE", viewModel.getDistance("Run").toString())
        }

        runAchievementViewModel.runAchievemetsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                runAchievementsAdapter.submitList(it as MutableList<AchievementRow>)
                Log.i("DISTANCE1",viewModel.getDistance("Run").toString())
            }
        }

        //FIXME HERE IT IS WORKING
        viewModel.getDistance("Run").observe(this.viewLifecycleOwner) {totalDistance ->
            _totalDistance = totalDistance
            Log.i("DISTANCE1",_totalDistance.toString())
        }



        //view.findViewById<FloatingActionButton>(R.id.floatingButtonAdd).setOnClickListener {
        //    findNavController().navigate(R.id.action_mainFragment_to_addTrainingLog)
        //}

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.getDistance("Run")
    }

    /* Opens XXXXXXXXXXXXXXX when AchievementRow item is clicked. */
    private fun adapterOnClick(runAchievement: AchievementRow) {

        /*Insert trainingLogRow id to bundle and pass data to destination*/
        //val bundle = bundleOf("TRAINING_LOG_ID" to runAchievement.id)
        //findNavController().navigate(R.id.action_mainFragment_to_trainingLogItem, bundle)
    }

}



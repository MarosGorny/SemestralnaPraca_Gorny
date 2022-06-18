package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory

class RunAchievementList : Fragment() {


    private val runAchievementViewModel by viewModels<RunAchievementListViewModel> {
        RunAchievementListViewModelFactory(
            requireContext())
    }

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val logViewModel: LogViewModel by activityViewModels {
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

        //Adapter
        val runAchievementsAdapter = RunAchievementAdapter { runAchievementList -> adapterOnClick(runAchievementList)}

        //RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.achievement_list)
        recyclerView.adapter = runAchievementsAdapter

        //Nastav list aby zobrazovalo


        //Dostan distance z databazy
        logViewModel.getDistance("Run").observe(this.viewLifecycleOwner) { totalDistance ->

            //FIXME ked je zoznam prazdny hodi error
            if(totalDistance != null)
                runAchievementsAdapter.updateDistanceOfRunning(totalDistance)
            Log.i("RunAchievementList.kt",totalDistance.toString())
        }


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



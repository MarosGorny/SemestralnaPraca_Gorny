package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentRunAchievementBinding
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory

class RunAchievementList : Fragment() {

    private val runAchievementViewModel : RunAchievementListViewModel by activityViewModels {
        RunAchievementListViewModelFactory(
            (activity?.application as LogListApplication).databaseAchievements.achievementDao()
        )
    }

    private var _binding: FragmentRunAchievementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentRunAchievementBinding.inflate(inflater,container,false)
        runAchievementViewModel.addAchievement()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val achievementAdapter = RunAchievementAdapter()

        binding.achievementList.layoutManager = LinearLayoutManager(this.context)
        binding.achievementList.adapter = achievementAdapter

        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes
        runAchievementViewModel.allAchievements.observe(this.viewLifecycleOwner) { items->
            items.let {
                achievementAdapter.submitList(it)
            }
        }

        //Floating buttons if I want
    }

}



package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentRunAchievementBinding
import com.example.traininglog.gorny.treningovy_zapisnik.LogListApplication

/**
 * Fragment ktory zobrazuje detail pre vsetky achievemnty ktore su v databaze
 */
class RunAchievementList : Fragment() {

    private val runAchievementViewModel : RunAchievementListViewModel by activityViewModels {
        RunAchievementListViewModelFactory(
            (activity?.application as LogListApplication).databaseAchievements.achievementDao()
        )
    }

    private var _binding: FragmentRunAchievementBinding? = null
    private val binding get() = _binding!!


    /**
     * Pri vytvoreni sa naplni layout pre RunAchievementList fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunAchievementBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * Hned ako sa fragment vytvori, je zavolana OnViewCreated funkcia
     * ktora nainicializuje Adapter pre list achievementov
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val achievementAdapter = RunAchievementAdapter()

        binding.achievementList.layoutManager = LinearLayoutManager(this.context)
        binding.achievementList.adapter = achievementAdapter

        //Observer listu kde su vsetky achievementy a ktory automaticky upravi data ked sa zmenia data
        runAchievementViewModel.allAchievements.observe(this.viewLifecycleOwner) { items->
            items.let {
                achievementAdapter.submitList(it)
            }
        }

    }

}



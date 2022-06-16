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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.data.runAchievementList
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunAchievementList : Fragment() {

    private val runAchievementViewModel by viewModels<RunAchievementListViewModel> {
        RunAchievementListViewModelFactory(requireContext())
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

        runAchievementViewModel.runAchievemetsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                runAchievementsAdapter.submitList(it as MutableList<AchievementRow>)
            }
        })

        //view.findViewById<FloatingActionButton>(R.id.floatingButtonAdd).setOnClickListener {
        //    findNavController().navigate(R.id.action_mainFragment_to_addTrainingLog)
        //}

        return view
    }

    /* Opens XXXXXXXXXXXXXXX when AchievementRow item is clicked. */
    private fun adapterOnClick(runAchievement: AchievementRow) {

        /*Insert trainingLogRow id to bundle and pass data to destination*/
        //val bundle = bundleOf("TRAINING_LOG_ID" to runAchievement.id)
        //findNavController().navigate(R.id.action_mainFragment_to_trainingLogItem, bundle)
    }

}
//class RunAchievementAdapter(private val myDataset: Array<AchievementRow>)
class RunAchievementAdapter(private val onClick: (AchievementRow) -> Unit) :
    ListAdapter<AchievementRow,RunAchievementAdapter.RunAchievementViewHolder>(
        RunAchievementDiffCallback
    ) {
    class RunAchievementViewHolder(itemView: View, val onClick: (AchievementRow) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val description:TextView = itemView.findViewById(R.id.achievement_description)
        private val imageOftype:ImageView = itemView.findViewById(R.id.achievement_image)

        private var currentAchievemntRov: AchievementRow? =null

        init {
            itemView.setOnClickListener {
                currentAchievemntRov?.let {
                    onClick(it)
                }
            }
        }

        /*Bind achievements stats*/
        fun bind(runAchievement: AchievementRow) {
            currentAchievemntRov = runAchievement

            description.text = runAchievement.description
            imageOftype.setImageResource(runAchievement.imageOfType)
        }
    }

        /* Creates and inflates view and return RunAchievementViewHolder */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RunAchievementViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.achievement_item,parent,false)
            return RunAchievementViewHolder(view,onClick)
        }

        /* Gets current achievement and uses it to bind view. */
        override fun onBindViewHolder(holder: RunAchievementViewHolder, position: Int) {
            val runAchievement = getItem(position)
            holder.bind(runAchievement)
        }

}

object RunAchievementDiffCallback: DiffUtil.ItemCallback<AchievementRow>() {
    override fun areItemsTheSame(oldItem: AchievementRow, newItem: AchievementRow): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AchievementRow, newItem: AchievementRow): Boolean {
        return oldItem.id == newItem.id
    }
}


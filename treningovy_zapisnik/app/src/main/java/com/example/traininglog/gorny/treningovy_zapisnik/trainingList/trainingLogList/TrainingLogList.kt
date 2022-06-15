package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrainingLogList : Fragment() {

    private val trainingLogListViewModel by viewModels<TrainingListViewModel> {
        TrainingListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_traininglog_list, container, false)

        /* Instantiates headerAdapter and trainingLogsRowsAdapter. Both adapters are added to concatAdapter.
    which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val trainingLogsRowsAdapter = TrainingLogsRowsAdapter { trainingLogRow -> adapterOnClick(trainingLogRow) }
        val concatAdapter = ConcatAdapter(headerAdapter, trainingLogsRowsAdapter)

        val recyclerView: RecyclerView = view.findViewById(R.id.trainingLog_list)
        recyclerView.adapter = concatAdapter

        trainingLogListViewModel.trainingLogsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                trainingLogsRowsAdapter.submitList(it as MutableList<TrainingLogRow>)
                headerAdapter.updateTrainingLogCount(it.size)
            }
        })

        view.findViewById<FloatingActionButton>(R.id.floatingButtonAdd).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTrainingLog)
        }

        return view
    }


    /* Opens TrainingLogDetailActivity when TrainingList item is clicked. */
    private fun adapterOnClick(trainingLogRow: TrainingLogRow) {

        /*Insert trainingLogRow id to bundle and pass data to destination*/
        val bundle = bundleOf("TRAINING_LOG_ID" to trainingLogRow.id)
        findNavController().navigate(R.id.action_mainFragment_to_trainingLogItem, bundle)
    }

    /* Adds trainingLog to trainingLogList when FAB is clicked. */
    private fun floatingButtonAddOnClick() {

    }

}

class TrainingLogsRowsAdapter(private val onClick: (TrainingLogRow) -> Unit) :
    ListAdapter<TrainingLogRow, TrainingLogsRowsAdapter.TrainingLogRowViewHolder>(
        TrainingLogDiffCallback
    ) {

    /* ViewHolder for TrainingLog, takes in the inflated view and the onClick behavior. */
    class TrainingLogRowViewHolder(itemView: View, val onClick: (TrainingLogRow) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val logType: TextView = itemView.findViewById(R.id.workoutType)
        private val timeTitle: TextView = itemView.findViewById(R.id.timeTitle)
        private val distanceTitle: TextView = itemView.findViewById(R.id.distanceTitle)
        private val statsTitle: TextView = itemView.findViewById(R.id.statsTitle)

        private val dateOfLog: TextView = itemView.findViewById(R.id.dateOfWorkout)
        private val timeOfLog: TextView = itemView.findViewById(R.id.startTimeOfWorkout)

        private val durationOfWorkout: TextView = itemView.findViewById(R.id.timeSpendWorkout)
        private val distanceOfWorkout: TextView = itemView.findViewById(R.id.distanceStats)
        private val statsOfWorkout: TextView = itemView.findViewById(R.id.stats)

        private var currentTrainingLogRow: TrainingLogRow? = null

        init {
            itemView.setOnClickListener {
                currentTrainingLogRow?.let {
                    onClick(it)
                }
            }
        }

        /* Bind trainingLog stats. */
        fun bind(trainingLogRow: TrainingLogRow) {
            currentTrainingLogRow = trainingLogRow

            logType.text = trainingLogRow.logTypeTitle
            timeTitle.text = trainingLogRow.timeTitle
            distanceTitle.text = trainingLogRow.distanceMetricTitle
            statsTitle.text = trainingLogRow.fourthColumnTitle

            dateOfLog.text = trainingLogRow.dateOfLog
            timeOfLog.text = trainingLogRow.timeOfLog

            durationOfWorkout.text = trainingLogRow.durationOfLog
            distanceOfWorkout.text = trainingLogRow.distance.toString()
            statsOfWorkout.text = trainingLogRow.fourthColumnMinPKm
        }

    }

    /* Creates and inflates view and return TrainingLogRowViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingLogRowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_log_item,parent,false)
        return TrainingLogRowViewHolder(view,onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: TrainingLogRowViewHolder, position: Int) {
        val trainingLogRow = getItem(position)
        holder.bind(trainingLogRow)
    }
}

object TrainingLogDiffCallback: DiffUtil.ItemCallback<TrainingLogRow>() {
    override fun areItemsTheSame(oldItem: TrainingLogRow, newItem: TrainingLogRow): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TrainingLogRow, newItem: TrainingLogRow): Boolean {
        return oldItem.id == newItem.id
    }
}
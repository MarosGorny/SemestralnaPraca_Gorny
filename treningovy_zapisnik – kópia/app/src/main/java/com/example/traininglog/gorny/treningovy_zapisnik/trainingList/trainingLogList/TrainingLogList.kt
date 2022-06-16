package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentTraininglogListBinding
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * Main fragment displaying details for all items in the database.
 */
class TrainingLogList : Fragment() {

    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao()
        )
    }

    private var _binding: FragmentTraininglogListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTraininglogListBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Instantiates headerAdapter and trainingLogsRowsAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val trainingLogsRowsAdapter = TrainingLogsRowsAdapter { trainingLogRow -> adapterOnClick(trainingLogRow) }
        val concatAdapter = ConcatAdapter(headerAdapter, trainingLogsRowsAdapter)

        binding.trainingLogList.layoutManager = LinearLayoutManager (this.context)
        binding.trainingLogList.adapter = concatAdapter

        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes
        viewModel.allTrainingLogs.observe(this.viewLifecycleOwner) { items ->
            items.let {
                trainingLogsRowsAdapter.submitList(it)
            }
        }

        binding.floatingButtonAdd.setOnClickListener {
            val action = TrainingLogListDirections.actionMainFragmentToAddTrainingLog("Add Training Log")
            this.findNavController().navigate(action)
        }


    }

    /* Opens TrainingLogDetailActivity when TrainingList item is clicked. */
    private fun adapterOnClick(trainingLogRow: TrainingLogRow) {
        val action = TrainingLogListDirections.actionTrainingLogListToTrainingLogItem()
        this.findNavController().navigate(action)
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
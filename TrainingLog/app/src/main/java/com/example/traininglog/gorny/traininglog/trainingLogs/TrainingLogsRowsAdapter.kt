package com.example.traininglog.gorny.traininglog.trainingLogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.traininglog.R
import com.example.traininglog.gorny.traininglog.data.TrainingLogRow


class TrainingLogsRowsAdapter(private val onClick: (TrainingLogRow) -> Unit) :
    ListAdapter<TrainingLogRow, TrainingLogsRowsAdapter.TrainingLogRowViewHolder>(TrainingLogDiffCallback) {

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

        /* Bind trainingLog TCstats. */
        fun bind(trainingLogRow: TrainingLogRow) {
            currentTrainingLogRow = trainingLogRow

            logType.text = trainingLogRow.logTypeTitle
            timeTitle.text = "Time"
            distanceTitle.text = "t23"
            statsTitle.text = "min/km"

            dateOfLog.text = "12.12.2024"
            timeOfLog.text = "15:23"

            durationOfWorkout.text = "00:23:14"
            distanceOfWorkout.text = 24.5.toString()
            statsOfWorkout.text = "6:12"
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

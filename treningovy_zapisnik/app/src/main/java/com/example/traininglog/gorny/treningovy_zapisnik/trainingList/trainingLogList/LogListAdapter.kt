package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.TrainingLogItemBinding


class LogListAdapter(private val onItemClicked: (TrainingLogRow) -> Unit) :
    ListAdapter<TrainingLogRow, LogListAdapter.LogViewHolder>(
        DiffCallback
    ) {

    /* Creates and inflates view and return LogViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(
            TrainingLogItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    /* Gets current TrainingLog and uses it to bind view. */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val trainingLogRow = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(trainingLogRow)
        }
        holder.bind(trainingLogRow)
    }



    class LogViewHolder(private var binding: TrainingLogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(trainingLogRow: TrainingLogRow) {
                binding.workoutType.text = trainingLogRow.logTypeTitle
                binding.timeTitle.text = trainingLogRow.timeTitle
                binding.distanceTitle.text = trainingLogRow.distanceMetricTitle
                binding.statsTitle.text = trainingLogRow.fourthColumnTitle

                binding.dateOfWorkout.text = trainingLogRow.dateOfLog
                binding.startTimeOfWorkout.text = trainingLogRow.timeOfLog

                binding.timeSpendWorkout.text = trainingLogRow.durationOfLog
                binding.distanceStats.text = trainingLogRow.distance.toString()
                binding.stats.text = trainingLogRow.fourthColumnMinPKm

            }
        }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TrainingLogRow>() {
            override fun areItemsTheSame(oldItem: TrainingLogRow, newItem: TrainingLogRow): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: TrainingLogRow, newItem: TrainingLogRow): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}


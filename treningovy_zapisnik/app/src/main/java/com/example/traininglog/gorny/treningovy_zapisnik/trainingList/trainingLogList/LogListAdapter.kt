package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.TrainingLogItemBinding

/**
 * LogListAdapter pre list workoutov
 */
class LogListAdapter(private val onItemClicked: (TrainingLogRow) -> Unit) :
    ListAdapter<TrainingLogRow, LogListAdapter.LogViewHolder>(DiffCallback) {


    /**
     * Vytvori a inflantne view a nasledne vrati LogViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(
            TrainingLogItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    /**
     * Vybere aktualny workout a nabinduje view
     */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val trainingLogRow = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(trainingLogRow)
        }
        holder.bind(trainingLogRow)
    }


    /**
     * LogViewHolder si pamata view a informacie daneho itemu z listu
     */
    class LogViewHolder(private var binding: TrainingLogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Nabindovanie daneho workoutu
         */
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


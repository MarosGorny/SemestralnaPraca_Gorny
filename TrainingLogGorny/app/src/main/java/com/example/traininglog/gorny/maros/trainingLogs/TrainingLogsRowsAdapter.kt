package com.example.traininglog.gorny.maros.trainingLogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.maros.R
import com.example.traininglog.gorny.maros.data.TrainingLogRow


class TrainingLogsRowsAdapter(private val onClick: (TrainingLogRow) -> Unit) :

    ListAdapter<TrainingLogRow,TrainingLogsRowsAdapter.TrainingLogRowViewHolder>(TrainingLogDiffCallback) {

    /* ViewHolder for TrainingLog, takes in the inflated view and the onClick behavior. */
    class TrainingLogRowViewHolder(itemView: View, val onClick: (TrainingLogRow) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private  val trainingLogRowTextView: TextView = itemView.findViewById(R.id.distance_input_numer)
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

            trainingLogRowTextView.text = trainingLogRow.thirdColumnStat.toString()
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

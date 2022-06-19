package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    private var trainingLogsCount: Int = 0

    /* ViewHolder for displaying header. */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val trainingLogNumberTextView: TextView = itemView.findViewById(R.id.training_number_text)

        fun bind(trainingLogsCount: Int) {
            trainingLogNumberTextView.text = trainingLogsCount.toString()
        }
    }

    /* Inflates view and returns HeaderViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_item,parent,false)
        return HeaderViewHolder(view)
    }

    /* Binds number of training logs to the header. */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(trainingLogsCount)
    }

    /* Returns number of items, but there is just only, hence return one */
    override fun getItemCount(): Int {
        return 1
    }

    fun updateTrainingLogCount(updatedTrainingLogCount: Int) {
        trainingLogsCount = updatedTrainingLogCount
        //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
        notifyDataSetChanged()
    }
}
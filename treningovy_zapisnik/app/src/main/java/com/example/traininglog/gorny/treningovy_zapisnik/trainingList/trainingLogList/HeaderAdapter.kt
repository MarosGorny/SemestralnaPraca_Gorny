package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R

/**
 * HeaderAdapter pre header na fragmente TrainingLogList ktory ukazuje pocet workoutov
 */
class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    private var trainingLogsCount: Int = 0


    /**
     * Holder pre zobrazovanie pocet workoutov
     */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val trainingLogNumberTextView: TextView = itemView.findViewById(R.id.training_number_text)

        /**
         * Bindovanie textu pre pocet workoutov
         */
        fun bind(trainingLogsCount: Int) {
            trainingLogNumberTextView.text = trainingLogsCount.toString()
        }
    }

    /**
     * Vytvori a inflante view a nasledne vrati HeaderViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_item,parent,false)
        return HeaderViewHolder(view)
    }

    /**
     * Nabinduje pocet workoutov do headru
     */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(trainingLogsCount)
    }

    /**
     * Vrati pocet itemov - 1
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * Aktializuje pocitadlo pre pocet workoutov a upozorni observerov ze sa zmenili data a mali by sa refreshnut
     */
    fun updateTrainingLogCount(updatedTrainingLogCount: Int) {
        trainingLogsCount = updatedTrainingLogCount
        notifyDataSetChanged()
    }
}
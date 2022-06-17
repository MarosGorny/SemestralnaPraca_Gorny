package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow

class RunAchievementAdapter(private val onClick: (AchievementRow) -> Unit) :
    ListAdapter<AchievementRow, RunAchievementAdapter.RunAchievementViewHolder>(
        RunAchievementDiffCallback
    ) {
    private var totalDistanceRunning: Double = 0.0

    class RunAchievementViewHolder(itemView: View, val onClick: (AchievementRow) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.achievement_description)
        private val imageOftype: ImageView = itemView.findViewById(R.id.achievement_image)
        private val current: TextView = itemView.findViewById(R.id.achievement_current)
        private val maxTextView: TextView = itemView.findViewById(R.id.achievement_max)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.achievement_linear_layout)


        private var currentAchievementRow: AchievementRow? =null


        init {
            itemView.setOnClickListener {
                currentAchievementRow?.let {
                    onClick(it)
                }
            }
        }

        /*Bind achievements stats*/
        fun bind(runAchievement: AchievementRow) {
            currentAchievementRow = runAchievement

            current.text = runAchievement.current.toString()
            maxTextView.text = runAchievement.max.toString()
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
        if(runAchievement.current >= runAchievement.max)
            holder.linearLayout.setBackgroundColor(Color.GREEN)
        else
            holder.linearLayout.setBackgroundColor(Color.WHITE)

        holder.bind(runAchievement)
    }


    fun updateDistanceOfRunning(updatedDistanceOfRunning: Double) {
        totalDistanceRunning = updatedDistanceOfRunning
        //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
        notifyDataSetChanged()
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

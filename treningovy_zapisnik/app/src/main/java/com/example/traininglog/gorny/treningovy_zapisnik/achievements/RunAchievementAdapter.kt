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
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.AchievementItemBinding
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentRunAchievementBinding

class RunAchievementAdapter() :
    //If on click, add this to parameter -> private val onClick: (AchievementRow) -> Unit
    ListAdapter<AchievementRow, RunAchievementAdapter.RunAchievementViewHolder>(
        RunAchievementDiffCallback
    ) {

    /* Creates and inflates view and return RunAchievementViewHolder */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RunAchievementViewHolder {
        return RunAchievementViewHolder(
            AchievementItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    /* Gets current achievement and uses it to bind view. */
    override fun onBindViewHolder(holder: RunAchievementViewHolder, position: Int) {
        val runAchievement = getItem(position)

        /* To future, if I want to do something on click
        holder.itemView.setOnClickListener {
            onClick(runAchievement)
        }
         */

        holder.bind(runAchievement)
    }



    class RunAchievementViewHolder(private var binding: AchievementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /*Bind achievements stats*/
        fun bind(achievementRow: AchievementRow) {
            if (achievementRow.completed)
                binding.achievementLinearLayout.setBackgroundColor(Color.GREEN)
            else
                binding.achievementLinearLayout.setBackgroundColor(Color.GRAY)

            binding.achievementMax.text = achievementRow.max.toString()
            binding.achievementDescription.text = achievementRow.description
            binding.achievementImage.setImageResource(achievementRow.imageOfType)
            binding.achievementCurrent.text = achievementRow.current.toString()

            }
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

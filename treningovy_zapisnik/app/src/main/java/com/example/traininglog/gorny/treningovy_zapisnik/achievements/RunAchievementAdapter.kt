package com.example.traininglog.gorny.treningovy_zapisnik.achievements

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.AchievementRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.AchievementItemBinding


/**
 * RunAchievementAdapter pre list achievementov
 */
class RunAchievementAdapter :
    ListAdapter<AchievementRow, RunAchievementAdapter.RunAchievementViewHolder>(
        RunAchievementDiffCallback
    ) {

    /**
     * Vytvori a inflante view a nasledne vrati RunAchievementViewHolder
     */
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

    /**
     * Vyberie aktualny achievement a nabinduje view
     */
    override fun onBindViewHolder(holder: RunAchievementViewHolder, position: Int) {
        val runAchievement = getItem(position)

        holder.bind(runAchievement)
    }


    /**
     * RunAchievementViewHolder si pamata view a informacie daneho achievementu z listu
     */
    class RunAchievementViewHolder(private var binding: AchievementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Nabindovanie daneho achievementu a zmena farby podla splnenia
         */
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

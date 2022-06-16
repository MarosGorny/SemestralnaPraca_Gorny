package com.example.traininglog.gorny.traininglog.achievement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.gorny.traininglog.R


class RunAchievement : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_run_achievement,container,false)

        val viewAdapter = MyRunAdapter(Array(10) {
            "something"
        })

        view.findViewById<RecyclerView>(R.id.achievement_run_list).run {

            adapter = viewAdapter
        }
        return view
    }

}

class MyRunAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<MyRunAdapter.ViewHolder>() {


    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.achievement_item,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.findViewById<TextView>(R.id.achievement_text).text = myDataset[position]

        holder.itemView.findViewById<ImageView>(R.id.achievement_image).setImageResource(R.drawable.run)

        //mozem este pouzit holder.item.SetOnCluckListener
        /*
        *         holder.item.setOnClickListener {
            val bundle = bundleOf(USERNAME_KEY to myDataset[position])

            holder.item.findNavController().navigate(
                    R.id.action_leaderboard_to_userProfile,
                bundle)
        }
        * */
    }

    override fun getItemCount() = myDataset.size

    companion object {
        const val ACHIEVEMENT_RUN = "runAchievement"
    }
}
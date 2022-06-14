package com.example.traininglog.gorny.treningovy_zapisnik.trainingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrainingLogList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_traininglog_list, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingButtonAdd).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTrainingLog)
        }

        return view
    }

}
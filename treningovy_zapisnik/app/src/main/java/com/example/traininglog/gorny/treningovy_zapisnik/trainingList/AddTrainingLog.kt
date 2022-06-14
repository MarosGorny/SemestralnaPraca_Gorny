package com.example.traininglog.gorny.treningovy_zapisnik.trainingList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.traininglog.gorny.treningovy_zapisnik.R

class AddTrainingLog : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_training_log, container, false)
    }

}
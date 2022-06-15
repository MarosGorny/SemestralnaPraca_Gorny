package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentAddTrainingLogBinding
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail.TrainingLogDetailArgs
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory

import java.text.SimpleDateFormat
import java.util.*


class AddTrainingLog : Fragment() {


    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao()
        )
    }

    private val navigationArgs: TrainingLogDetailArgs by navArgs()

    lateinit var trainingLogRow: TrainingLogRow

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddTrainingLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTrainingLogBinding.inflate(inflater,container,false)
        return binding.root

    }

    /**
     * Returns true if the EditText is not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.distanceInputNumber.text.toString()
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_done).setOnClickListener {




            addTrainingLog()

        }


    }

    /* The onClick action for the done button. Closes the activity and returns the new training name
           and description as part of the intent. If the distance is missing, the result is set
           to cancelled. */
    private fun addTrainingLog() {
        Log.i("","1")

        val resultIntent = Intent()

            findNavController().navigate(R.id.action_addTrainingLog_to_trainingLogList)
        }



}
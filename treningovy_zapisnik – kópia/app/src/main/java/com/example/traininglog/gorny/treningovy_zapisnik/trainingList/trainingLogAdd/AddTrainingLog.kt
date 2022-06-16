package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

    /**
     * Binds views with the passed in [item] information.
     */
    private fun bind(trainingLogRow: TrainingLogRow) {
        //val price = "%.2f".format(item.itemPrice)
        binding.apply {
            typeActivityTitle.setText(trainingLogRow.logTypeTitle,TextView.BufferType.SPANNABLE)
            dateButton.setText(trainingLogRow.dateOfLog,TextView.BufferType.SPANNABLE)
            timeButton.setText(trainingLogRow.timeOfLog,TextView.BufferType.SPANNABLE)
            distanceInputNumber.setText(trainingLogRow.distance.toString(),TextView.BufferType.SPANNABLE)

            buttonDone.setOnClickListener{updateTrainingLog()}

        }
    }

    /**
     * Inserts the new TrainingLog into database and navigates up to list fragment.
     */
    private fun addNewTrainingLog() {
        if (isEntryValid()) {
            viewModel.addNewTrainingLogRow(
                binding.typeActivityTitle.text.toString(),
                binding.dateButton.text.toString() ,
                binding.timeButton.text.toString(),
                binding.durationInputTime.text.toString(),
                binding.distanceInputNumber.text.toString().toDouble()
            )
            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing TrainingLogRow in the database and navigates up to list fragment.
     */
    private fun updateTrainingLog() {
        if (isEntryValid()) {
            viewModel.updateTrainingLogRow(
                this.navigationArgs.trainingLogId,
                this.binding.typeActivityTitle.text.toString(),
                this.binding.dateButton.text.toString(),
                this.binding.timeButton.text.toString(),
                this.binding.durationInputTime.text.toString(),
                "METRICS VYPOCITAT",
                this.binding.distanceInputNumber.text.toString().toDouble(),
                "treba funkciu",
                "treba funkciu"
            )
            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        }
    }


    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.trainingLogId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                trainingLogRow = selectedItem
                bind(trainingLogRow)
            }
        } else {
            binding.buttonDone.setOnClickListener {
                addNewTrainingLog()
            }
        }


    }
    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }



}
package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentTrainingLogDetailBinding
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TrainingLogDetail : Fragment() {
    private val navigationArgs: TrainingLogDetailArgs by navArgs()
    lateinit var trainingLogRow: TrainingLogRow

    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory (
            (activity?.application as LogListApplication).database.trainingLogRowDao()
        )
    }

    private var _binding: FragmentTrainingLogDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrainingLogDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * Binds views with the passed in item data.
     */
    private fun bind(trainingLogRow: TrainingLogRow) {
        binding.apply {
            titleDetail.text = trainingLogRow.logTypeTitle
            distanceDetail.text = trainingLogRow.distance.toString()
            timeDetail.text = trainingLogRow.durationOfLog

            removeButton.setOnClickListener{ showConfirmationDialog() }
            editTrainingLog.setOnClickListener{ editTrainingLog() }
        }
    }

    /**
     * Navigate to the Edit trainingLog screen.
     */
    private fun editTrainingLog() {
        val action = TrainingLogDetailDirections.actionTrainingLogItemToAddTrainingLog(
            "Edit",
            trainingLogRow.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you sure?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteTrainingLog()
            }
            .show()
    }

    /**
     * Deletes the current trainingLog and navigates to the list fragment.
     */
    private fun deleteTrainingLog() {
        viewModel.deleteItem(trainingLogRow)
        findNavController().navigateUp()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.trainingLogId
        // Retrieve the item details using the trainingLogId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            trainingLogRow = selectedItem
            bind(trainingLogRow)
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
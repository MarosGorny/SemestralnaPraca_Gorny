package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentTrainingLogDetailBinding
import com.example.traininglog.gorny.treningovy_zapisnik.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Ukaze detail vybraneho workoutu
 */
class TrainingLogDetail : Fragment() {
    private val navigationArgs: TrainingLogDetailArgs by navArgs()
    lateinit var trainingLogRow: TrainingLogRow

    private val viewModel : LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            (activity?.application as LogListApplication).databaseAchievements.achievementDao(),
        )
    }

    private var _binding: FragmentTrainingLogDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflante view pre dany fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrainingLogDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * Nabinduje view s datami ktore ma dany workout
     */
    private fun bind(trainingLogRow: TrainingLogRow) {
        binding.apply {
            titleDetail.text = trainingLogRow.logTypeTitle
            distanceDetail.text = trainingLogRow.distance.toString()
            timeDetail.text = trainingLogRow.durationOfLog
            paceDetail.text = trainingLogRow.fourthColumnMinPKm
            durationPostfixDetail.text = "hh:mm:ss"

            when(trainingLogRow.logTypeTitle) {
                "Run" -> {
                    distancePostfixDetail.text = "km"
                    pacePostfixDetail.text = "min/km"
                }
                "Bike" -> {
                    distancePostfixDetail.text = "km"
                    pacePostfixDetail.text = "km/h"
                }
                "Swim" -> {
                    distancePostfixDetail.text = "m"
                    pacePostfixDetail.text = "min/100m"
                }
            }

            removeButton.setOnClickListener{showConfirmationDialog() }
            editTrainingLog.setOnClickListener { editTrainigLog() }
        }
    }

    /**
     * Presmeruje na fragment kde sa upravuje workout.
     */
    private fun editTrainigLog() {
        val action = TrainingLogDetailDirections.actionTrainingLogItemToAddTrainingLog(
            "Edit training log",
            trainingLogRow.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Ukaze okno na potvrdenie/odmietnutie vymazania daneho workoutu
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
     * Vymaze dany workout a vrati sa spat na zoznam workoutov
     */
    private fun deleteTrainingLog() {
        viewModel.deleteItem(trainingLogRow)
        findNavController().navigateUp()
    }

    /**
     * Po vytvoreni fragmentu sa vytiahne z uloziska dany workout
     * a ked sa zmenia observer aktualizuje hodnoty podla zmenenych dat
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.logId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedTrainingLog ->
            trainingLogRow = selectedTrainingLog
            bind(trainingLogRow)
        }

    }

    /**
     * Binding nastavi na null
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
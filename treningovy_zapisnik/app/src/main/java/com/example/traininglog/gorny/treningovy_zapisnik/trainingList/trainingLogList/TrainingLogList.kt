package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentTraininglogListBinding
import com.example.traininglog.gorny.treningovy_zapisnik.LogListApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Hlavny fragment ktory zobrazuje detail pre vsetky workouty ktore su v databaze
 *  a z ktoreho sa moze workout pridat, odobrat, alebo upravit
 */
class TrainingLogList : Fragment() {

    //Nastavia sa obidve databazy cez LogViewFactory
    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            (activity?.application as LogListApplication).databaseAchievements.achievementDao(),
        )
    }

    private var _binding: FragmentTraininglogListBinding? = null
    private val binding get() = _binding!!


    /**
     * Pri vytvoreni sa naplni layout pre TrainingLogList fragment
     * Tiez sa tu odchytavaju vsetky vynimky
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            Log.e(
                "Error" + Thread.currentThread().stackTrace[2],
                paramThrowable.localizedMessage
            )
            Toast.makeText(requireContext(),"Error" + Thread.currentThread().stackTrace[2],Toast.LENGTH_LONG).show()
        }

        // Inflate the layout for this fragment
        _binding = FragmentTraininglogListBinding.inflate(inflater,container,false)
        return binding.root
    }


    /**
     * Hned ako sa fragment vytvori, je zavolana OnViewCreated funkcia
     * ktora nainicializuje Adapter aj pred header aj pre list
     * Taktiez sa nastavi bindovanie na zaklade tychto dvoch adapter
     * Reaguje na buttony a prepocitava dlzku workoutov pre achievementy
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Spojenie adapterov
        val headerAdapter = HeaderAdapter()
        val trainingLogsRowsAdapter = LogListAdapter {
            val action = TrainingLogListDirections.actionMainFragmentToTrainingLogItem(it.id)
            this.findNavController().navigate(action)
        }
        val concatAdapter = ConcatAdapter(headerAdapter, trainingLogsRowsAdapter)
        binding.trainingLogList.layoutManager = LinearLayoutManager (this.context)
        binding.trainingLogList.adapter = concatAdapter


        //Observer listu kde su vsetky workouty a ktory automaticky upravi data ked sa zmenia
        viewModel.allTrainingLogs.observe(this.viewLifecycleOwner) { items ->
            items.let {
                trainingLogsRowsAdapter.submitList(it)
                headerAdapter.updateTrainingLogCount(it.size)
            }
        }


        //Prepocitanie vzdialenosti pre vsetky typy
        val listOf = listOf("Run","Swim","Bike")
        for (logType in listOf) {
            viewModel.getDistance(logType).observe(this.viewLifecycleOwner) { totalDistance ->
                setAchievementDistanceByType(logType,totalDistance?: 0.0)
            }
        }

        //Presmerovanie do fragmentu na pridavanie workoutov
        binding.floatingButtonAdd.setOnClickListener {
            val action = TrainingLogListDirections.actionMainFragmentToAddTrainingLog("Add training log")
            findNavController().navigate(action)
        }

        //Vymazanie vsetkych workoutov
        binding.floatingButtonDelete.setOnClickListener {
            showConfirmationDialog()
        }

    }

    /**
     * Nastavi sa nova vzdialenost pre achievementy podla typu aktivity
     *
     * @param logType typ aktivity - String
     * @param newDistance nova vzdialenost = Double
     */
    private fun setAchievementDistanceByType(logType: String, newDistance:Double) {
        viewModel.setAchievementDistance(logType,newDistance)
    }

    /**
     * Vymaze vsetky workouty a vrati naspat na list fragment
     */
    private fun deleteAllTrainingLogs() {
        viewModel.deleteAllItems()
    }

    /**
     * Ukaze okno v ktorom treba potvrdit/odmietnut zmazanie vsetkych workoutov
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Deleting all training workouts")
            .setMessage("Are you sure that you want delete ALL training workouts?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteAllTrainingLogs()
            }
            .show()
    }





}


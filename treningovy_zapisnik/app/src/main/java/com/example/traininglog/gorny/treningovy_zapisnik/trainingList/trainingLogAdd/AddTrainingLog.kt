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
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.TrainingListViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

const val TRAINING_LOG_DISTANCE = "log distance"
const val TRAINING_LOG_DURATION = "log duration"
const val TRAINING_LOG_TIME = "log time"
const val TRAINING_LOG_DATE = "log date"
const val LOG_ACTIVITY = "log activity"

class AddTrainingLog : Fragment() {

    lateinit var addDistance: EditText
    lateinit var addDuration: EditText
    lateinit var addDate: Button
    lateinit var addTime: Button
    lateinit var logType: String

    var dateButtonClicked = false
    var timeButtonClicked = false

    private val viewModel: TrainingListViewModel by activityViewModels<TrainingListViewModel> {
        TrainingListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_training_log, container, false)



        view.findViewById<Button>(R.id.dateButton).setOnClickListener {
            dateButtonClicked=true
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                view.findViewById<Button>(R.id.dateButton).text = SimpleDateFormat("dd.MM.yy").format(cal.time)
            }
            DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        view.findViewById<Button>(R.id.timeButton).setOnClickListener {
            timeButtonClicked=true
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                view.findViewById<Button>(R.id.timeButton).text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }



        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_done).setOnClickListener {

            val selectedId = view.findViewById<RadioGroup>(R.id.activity_options).checkedRadioButtonId
            logType = when (selectedId) {
                R.id.option_run -> "Run"
                R.id.option_bike -> "Bike"
                else -> "Swim"
            }
            Log.i("","0")
            addDistance = view.findViewById(R.id.distance_input_number)
            addDuration = view.findViewById(R.id.duration_input_time)
            addDate = view.findViewById(R.id.dateButton)
            addTime = view.findViewById(R.id.timeButton)


            addTrainingLog()

        }


    }

    /* The onClick action for the done button. Closes the activity and returns the new training name
           and description as part of the intent. If the distance is missing, the result is set
           to cancelled. */
    private fun addTrainingLog() {
        Log.i("","1")

        val resultIntent = Intent()
        if(addDistance.text.isNullOrEmpty()) {
            Log.i("Toast","done")
            Toast.makeText(activity,"Insert distance",Toast.LENGTH_SHORT).show()
        } else {

            var date:String = if (dateButtonClicked)  {
                addDate.text.toString()
            } else null.toString()

            var time:String = if (timeButtonClicked) {
                addTime.text.toString()
            } else null.toString()

            Log.i("","4")
            val duration = addDuration.text.toString()
            val distance = addDistance.text.toString()

            viewModel.insertTrainingLog(logType,date,time,duration,distance.toDouble())
            findNavController().navigate(R.id.action_addTrainingLog_to_trainingLogList)
        }

    }

}
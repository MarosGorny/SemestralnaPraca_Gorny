package com.example.traininglog.gorny.traininglog.addActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.traininglog.gorny.traininglog.data.EnumAktivity
import com.example.traininglog.gorny.traininglog.R

const val TRAINING_LOG_DISTANCE = "distance"
const val LOG_ACTIVITY = "log activity"
class AddActivityLog: AppCompatActivity() {


    lateinit var radioButton: RadioButton
    lateinit var addDistance: EditText
    lateinit var logType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_training_log_layout)

        findViewById<Button>(R.id.dateButton).setOnClickListener {

        }

        findViewById<Button>(R.id.timeButton).setOnClickListener {

        }

        findViewById<Button>(R.id.button_done).setOnClickListener {
            addTrainingLog()
        }

        addDistance = findViewById(R.id.distance_input_number)
        val selectedId = findViewById<RadioGroup>(R.id.activity_options).checkedRadioButtonId
        logType = when (selectedId) {
            R.id.option_run -> "Run"
            R.id.option_bike -> "Bike"
            else -> "Swim"
        }
    }


    /* The onClick action for the done button. Closes the activity and returns the new training name
    and description as part of the intent. If the distance is missing, the result is set
    to cancelled. */
    private fun addTrainingLog() {


        val resultIntent = Intent()
        if(addDistance.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED,resultIntent)
        } else {
            val distance = addDistance.text.toString()
            resultIntent.putExtra(TRAINING_LOG_DISTANCE,distance)
            resultIntent.putExtra(LOG_ACTIVITY,logType)
            setResult(Activity.RESULT_OK,resultIntent)
        }
        finish()
    }
}
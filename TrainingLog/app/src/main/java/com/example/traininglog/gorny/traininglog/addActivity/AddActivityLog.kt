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
class AddActivityLog: AppCompatActivity() {

    //lateinit var bindig: ActivityMainBinding

    var radioGroup: RadioGroup? = null

    lateinit var radioButton: RadioButton
    lateinit var addDistance: EditText
    lateinit var enumActivity: EnumAktivity

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
    }


    /* The onClick action for the done button. Closes the activity and returns the new training name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */
    private fun addTrainingLog() {
        val selectedId = findViewById<RadioGroup>(R.id.activity_options).checkedRadioButtonId
        enumActivity = when (selectedId) {
            R.id.option_run -> EnumAktivity.RUN
            R.id.option_bike -> EnumAktivity.BIKE
            else -> EnumAktivity.SWIM
        }

        val resultIntent = Intent()
        if(addDistance.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED,resultIntent)
        } else {
            val distance = addDistance.text.toString()
            resultIntent.putExtra(TRAINING_LOG_DISTANCE,distance)
            setResult(Activity.RESULT_OK,resultIntent)
        }
        finish()
    }
}
package com.example.traininglog.gorny.maros.addActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.traininglog.gorny.maros.R
import com.example.traininglog.gorny.maros.data.EnumAktivity
import com.example.traininglog.gorny.maros.databinding.ActivityMainBinding

class AddActivityLog: AppCompatActivity() {

    //lateinit var bindig: ActivityMainBinding

    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton

    lateinit var addDistance: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_training_log_layout)

        findViewById<Button>(R.id.button_done).setOnClickListener {
            addTrainingLog()
        }

        addDistance = findViewById(R.id.distance_input_numer)
    }

    private fun addTrainingLog() {
        //TODO("Not yet implemented")
        val selectedId = findViewById<RadioGroup>(R.id.activity_options).checkedRadioButtonId
        val tipOfActivity = when (selectedId) {
            R.id.option_run -> EnumAktivity.RUN
            R.id.option_bike -> EnumAktivity.BIKE
            else -> EnumAktivity.SWIM
        }
    }
}
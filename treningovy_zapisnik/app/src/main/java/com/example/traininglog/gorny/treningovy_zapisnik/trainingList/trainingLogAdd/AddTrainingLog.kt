package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogAdd

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.traininglog.gorny.treningovy_zapisnik.*
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentAddTrainingLogBinding
import com.example.traininglog.gorny.treningovy_zapisnik.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail.TrainingLogDetailArgs
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory

import java.text.SimpleDateFormat
import java.util.*


const val TYPE_OF_LOG = "typeOfLog"
const val DATE_OF_LOG = "dateOfLog"
const val TIME_OF_LOG = "timeOfLog"
const val HOUR_OF_DURATION = "hourOfDuration"
const val MINUTE_OF_DURATION = "minuteOfDuration"
const val SECOND_OF_DURATION = "secondOfDuration"

/**
 * Fragment na pridavanie alebo aktualizovanie workoutov
 */
class AddTrainingLog : Fragment() {

    private val channelID = "channel_id_66"
    private val runNotificationID = 1
    private val bikeNotificationID = 2
    private val swimNotificationID = 3

    // pomocout by activityViewModels mozem viewModel pouzivat v roznych fragmentoch
    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            (activity?.application as LogListApplication).databaseAchievements.achievementDao()
        )
    }

    private val navigationArgs: TrainingLogDetailArgs by navArgs()

    private lateinit var trainingLogRow: TrainingLogRow

    private var _binding: FragmentAddTrainingLogBinding? = null
    private val binding get() = _binding!!


    /**
     * Pri vytvoreni sa naplni layout pre tento fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrainingLogBinding.inflate(inflater,container,false)
        if (savedInstanceState != null) {
            when(savedInstanceState.getString(TYPE_OF_LOG)) {
                RUN -> binding.activityOptions.check(R.id.option_run)
                BIKE -> binding.activityOptions.check(R.id.option_bike)
                SWIM -> binding.activityOptions.check(R.id.option_swim)
            }

            binding.dateButton.text = savedInstanceState.getString(DATE_OF_LOG)
            binding.timeButton.text = savedInstanceState.getString(TIME_OF_LOG)
            binding.numberPickerHour.value = savedInstanceState.getInt(HOUR_OF_DURATION)
            binding.numberPickerMinutes.value = savedInstanceState.getInt(MINUTE_OF_DURATION)
            binding.numberPickerSeconds.value = savedInstanceState.getInt(SECOND_OF_DURATION)


        }
        createNotificationChannel()
        return binding.root
    }

    /**
     * Vrati true ak je vzdialenost spravna
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.distanceInputNumber.text.toString()
        )
    }

    /**
     * Bindovanie view s datami z workoutu
     */
    private fun bind(trainingLogRow: TrainingLogRow) {

        binding.apply {

            when(trainingLogRow.logTypeTitle) {
                RUN -> activityOptions.check(R.id.option_run)
                BIKE -> activityOptions.check(R.id.option_bike)
                SWIM -> activityOptions.check(R.id.option_swim)
            }

            timeButton.setText(trainingLogRow.timeOfLog,TextView.BufferType.SPANNABLE)
            dateButton.setText(trainingLogRow.dateOfLog,TextView.BufferType.SPANNABLE)
            distanceInputNumber.setText(trainingLogRow.distance.toString(),TextView.BufferType.SPANNABLE)

            val hashMap: HashMap<String,Int> = parseDurationStringToHashMap(trainingLogRow.durationOfLog)
            numberPickerHour.value = hashMap[HOUR]!!
            numberPickerMinutes.value =hashMap[MINUTE]!!
            numberPickerSeconds.value = hashMap[SECOND]!!

            buttonDone.setOnClickListener{updateTrainingLogRow()}
        }
    }

    /**
     * Vlozi novy item do databazy a vrati sa naspat na fragment s listom workoutov
     */
    private fun addNewTrainingLogRow() {
        if (isEntryValid()) {
            viewModel.addNewTrainingLogRow(
                binding.typeActivityTitle.text.toString(),
                binding.dateButton.text.toString(),
                binding.timeButton.text.toString(),
                parseDurationNumbersToString(
                    binding.numberPickerHour.value,
                    binding.numberPickerMinutes.value,
                    binding.numberPickerSeconds.value),
                binding.distanceInputNumber.text.toString().toDouble()
            )

            sendNotification(binding.typeActivityTitle.text.toString(),binding.distanceInputNumber.text.toString().toDouble())
            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(),"Distance is required!",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Aktualizuje workout v databaze a vrati sa naspat na fragment s listom workoutov
     */
    private fun updateTrainingLogRow() {
        if (isEntryValid()) {
            val logType = this.binding.typeActivityTitle.text.toString()
            val distance = this.binding.distanceInputNumber.text.toString().toDouble()
            val duration = parseDurationNumbersToString(
                                this.binding.numberPickerHour.value,
                                this.binding.numberPickerMinutes.value,
                                this.binding.numberPickerSeconds.value)
            viewModel.updateTrainingLogRow(
                this.navigationArgs.logId,
                logType,
                this.binding.dateButton.text.toString(),
                this.binding.timeButton.text.toString(),
                duration,
                this.binding.distanceActivityTitle.text.toString(),
                distance,
                getTempoPostFix(logType),
                getPaceOfType(logType,distance,duration)
            )

            sendNotification(logType,distance)
            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(),"Distance is required!",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Funkcia ktora zavola TimePickerDialog a s nim si budem moct vybrat dany cas ktory sa nabinduje ako text pre button
     */
    private fun setTime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            binding.timeButton.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    /**
     * Funkcia ktora zavola DAtePickerDialog a s nim si budem moct vybrat dany datum ktory sa nabinduje ako text pre button
     */
    private fun setDate() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker,year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.dateButton.text = SimpleDateFormat("dd.MM.yyyy").format(cal.time)
        }
        DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    /**
     * Called when the view is created.

     * Metoda zavolana potom ako sa vytvorilo view a na zaklade id workoutu sa rozhodne ci,
     * sa workout ide upravovat (vytiahne data z databazy), alebo vytvarat novy
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.logId

        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {selectedTrainingLog ->
                trainingLogRow = selectedTrainingLog
                bind(trainingLogRow)
        }
        } else {
            binding.buttonDone.setOnClickListener {
                addNewTrainingLogRow()
            }
        }

        changeTitleTypeByRadioButton()
        binding.timeButton.setOnClickListener{ setTime() }
        binding.dateButton.setOnClickListener{ setDate() }

    }

    /**
     * Zmenenie nadpistu workoutu podla toho aky je zvoleny radio button
     */
    private fun changeTitleTypeByRadioButton() {
        if (binding.optionBike.isChecked)
            binding.typeActivityTitle.text = "Bike"
        else if (binding.optionRun.isChecked)
            binding.typeActivityTitle.text = "Run"
        else if (binding.optionSwim.isChecked)
            binding.typeActivityTitle.text = "Swim"

        binding.optionRun.setOnClickListener {
            binding.typeActivityTitle.text = "Run"
        }

        binding.optionBike.setOnClickListener {
            binding.typeActivityTitle.text = "Bike"
        }

        binding.optionSwim.setOnClickListener {
            binding.typeActivityTitle.text = "Swim"
        }
    }




    /**
     * Bindovanie nastavni na null a skryje klavesnicu
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    /**
     * Metoda zavolana na ulozenie dat pred tym ako je aktivita zabita
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TYPE_OF_LOG,binding.typeActivityTitle.text.toString())
        outState.putString(DATE_OF_LOG,binding.dateButton.text.toString())
        outState.putString(TIME_OF_LOG,binding.timeButton.text.toString())
        outState.putInt(HOUR_OF_DURATION,binding.numberPickerHour.value)
        outState.putInt(MINUTE_OF_DURATION,binding.numberPickerMinutes.value)
        outState.putInt(SECOND_OF_DURATION,binding.numberPickerSeconds.value)
    }

    /**
     * Vytvorenie kanalu pre notifikacie
     */
    private fun createNotificationChannel() {
        val name = "Notification title"
        val descriptionText = "Notification Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID,name,importance).apply {
            description = descriptionText
        }
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    /**
     * Zobrazenie notifikacie podla typu a splnenej vzdialenosti
     *
     * @param logType typ aktivity - String
     * @param distance vzdialenost aktivity - Double
     */
    private fun sendNotification(logType: String,distance:Double) {
        Log.i("SEND notification","Outer")
        when(logType) {
            RUN -> {
                if(distance >= 42.2)
                    Log.i("SEND notification","INNER")
                    sendNotificationByType(logType)
            }
            BIKE -> {
                if (distance >= 180.0) {
                    sendNotificationByType(logType)
                }
            }
            SWIM -> {
                if( distance >= 10000.0) {
                    sendNotificationByType(logType)
                }
            }
        }
    }

    /**
     * Zobrazenie notifikacie podla typy
     *
     * @param logType typ aktivity - String
     */
    private fun sendNotificationByType(logType:String) {
        var title= "ERROR"
        var description = "ERROR"
        var notificationID = 2
        var icon:Int = R.drawable.bike
        Log.i("SEND notificationbytype","outer")
        when(logType) {
            RUN -> {
                title = "Marathon is quite easy right?"
                description = "Keep going and be like Emil Zatopek!"
                icon = R.drawable.run
                notificationID = runNotificationID
                Log.i("SEND notificationbytype","inner")
            }
            BIKE -> {
                title = "Tour de France is ready for you!"
                description = "Keep going and be like Peter Sagan!"
                icon = R.drawable.bike
                notificationID = bikeNotificationID
            }
            SWIM -> {
                title = "You and the water = ONE ELEMENT"
                description = "Keep going and be like Jan Novak!"
                icon = R.drawable.swim
                notificationID = swimNotificationID
            }
        }


        val builder = NotificationCompat.Builder(requireContext(),channelID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationID,builder.build())
        }
    }

}
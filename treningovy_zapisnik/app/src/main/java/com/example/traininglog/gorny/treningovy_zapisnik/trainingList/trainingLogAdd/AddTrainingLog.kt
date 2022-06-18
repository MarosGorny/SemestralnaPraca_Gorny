package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.traininglog.gorny.treningovy_zapisnik.R
import com.example.traininglog.gorny.treningovy_zapisnik.data.TrainingLogRow
import com.example.traininglog.gorny.treningovy_zapisnik.databinding.FragmentAddTrainingLogBinding
import com.example.traininglog.gorny.treningovy_zapisnik.parseDurationNumbersToString
import com.example.traininglog.gorny.treningovy_zapisnik.parseDurationStringToHashMap
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
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
 * Fragment to add or update an item in the Inventory database.
 */
class AddTrainingLog : Fragment() {


    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: LogViewModel by activityViewModels {
        LogViewModelFactory(
            (activity?.application as LogListApplication).database.trainingLogRowDao(),
            requireContext()
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
        Log.i("onCreateView","")
        if (savedInstanceState != null) {
            Log.i("onCreateViewIn","")
            Log.i("TEXT-IN",binding.typeActivityTitle.text.toString())
            when(savedInstanceState.getString(TYPE_OF_LOG)) {
                "Run" -> binding.activityOptions.check(R.id.option_run)
                "Bike" -> binding.activityOptions.check(R.id.option_bike)
                "Swim" -> binding.activityOptions.check(R.id.option_swim)
            }

            binding.dateButton.text = savedInstanceState.getString(DATE_OF_LOG)
            binding.timeButton.text = savedInstanceState.getString(TIME_OF_LOG)
            binding.numberPickerHour.value = savedInstanceState.getInt(HOUR_OF_DURATION)
            binding.numberPickerMinutes.value = savedInstanceState.getInt(MINUTE_OF_DURATION)
            binding.numberPickerSeconds.value = savedInstanceState.getInt(SECOND_OF_DURATION)


        }

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
     * Binds views with the passed in TrainingLog information.
     */
    private fun bind(trainingLogRow: TrainingLogRow) {

        binding.apply {

            when(trainingLogRow.logTypeTitle) {
                "Run" -> activityOptions.check(R.id.option_run)
                "Bike" -> activityOptions.check(R.id.option_bike)
                "Swim" -> activityOptions.check(R.id.option_swim)
            }

            timeButton.setText(trainingLogRow.timeOfLog,TextView.BufferType.SPANNABLE)
            dateButton.setText(trainingLogRow.dateOfLog,TextView.BufferType.SPANNABLE)
            distanceInputNumber.setText(trainingLogRow.distance.toString(),TextView.BufferType.SPANNABLE)

            val hashMap: HashMap<String,Int> = parseDurationStringToHashMap(trainingLogRow.durationOfLog)
            numberPickerHour.value = hashMap["Hour"]!!
            numberPickerMinutes.value =hashMap["Minute"]!!
            numberPickerSeconds.value = hashMap["Second"]!!

            buttonDone.setOnClickListener{updateTrainingLogRow()}
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    @RequiresApi(Build.VERSION_CODES.O)
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

            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(),"Distance is required!",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateTrainingLogRow() {
        if (isEntryValid()) {
            viewModel.updateTrainingLogRow(
                this.navigationArgs.logId,
                this.binding.typeActivityTitle.text.toString(),
                this.binding.dateButton.text.toString(),
                this.binding.timeButton.text.toString(),
                parseDurationNumbersToString(
                    this.binding.numberPickerHour.value,
                    this.binding.numberPickerMinutes.value,
                    this.binding.numberPickerSeconds.value),
            this.binding.distanceActivityTitle.text.toString(),
            this.binding.distanceInputNumber.text.toString().toDouble(),
            "Treba nastait",
            "Treba nastavit")
        }
    }

    private fun setTime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            binding.timeButton.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

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
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("onViewCreated","")
        Log.i("onViewCreated",binding.typeActivityTitle.text.toString())
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.logId
        if (id > 0) {
            //TODO tu sa meni radio button na Run, z nejakeho dovodu, alebo teda na default TYPE
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) {selectedTrainingLog ->
                trainingLogRow = selectedTrainingLog
                Log.i("onViewCreatedObserver",trainingLogRow.logTypeTitle)
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

    private fun changeTitleTypeByRadioButton() {
        //Sometimes can happen that it is changed in advance
        if (binding.optionBike.isChecked)
            binding.typeActivityTitle.text = "Bike"
        else if (binding.optionRun.isChecked)
            binding.typeActivityTitle.text = "Run"
        else if (binding.optionSwim.isChecked)
            binding.typeActivityTitle.text = "Swim"

        binding.optionRun.setOnClickListener() {
            binding.typeActivityTitle.text = "Run"
        }

        binding.optionBike.setOnClickListener() {
            binding.typeActivityTitle.text = "Bike"
        }

        binding.optionSwim.setOnClickListener() {
            binding.typeActivityTitle.text = "Swim"
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        Log.i("onDestroyView","")
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("onSaveInstanceState","")
        super.onSaveInstanceState(outState)
        outState.putString(TYPE_OF_LOG,binding.typeActivityTitle.text.toString())
        outState.putString(DATE_OF_LOG,binding.dateButton.text.toString())
        outState.putString(TIME_OF_LOG,binding.timeButton.text.toString())
        outState.putInt(HOUR_OF_DURATION,binding.numberPickerHour.value)
        outState.putInt(MINUTE_OF_DURATION,binding.numberPickerMinutes.value)
        outState.putInt(SECOND_OF_DURATION,binding.numberPickerSeconds.value)

        Log.i("TEXT-OUT",binding.typeActivityTitle.text.toString())
    }
}
package com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
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
import com.example.traininglog.gorny.treningovy_zapisnik.parseDurationNumbersToString
import com.example.traininglog.gorny.treningovy_zapisnik.parseDurationStringToHashMap
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.LogListApplication
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogDetail.TrainingLogDetailArgs
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModel
import com.example.traininglog.gorny.treningovy_zapisnik.trainingList.trainingLogList.LogViewModelFactory

import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment to add or update an item in the Inventory database.
 */
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

        binding.apply {
            typeActivityTitle.setText(trainingLogRow.logTypeTitle,TextView.BufferType.SPANNABLE)
            when (trainingLogRow.logTypeTitle) {
                "Run" -> activityOptions.check(R.id.option_run)
                "Bike" -> activityOptions.check(R.id.option_bike)
                "Swim" -> activityOptions.check(R.id.option_swim)
            }
            dateButton.setText(trainingLogRow.dateOfLog,TextView.BufferType.SPANNABLE)
            timeButton.setText(trainingLogRow.timeOfLog,TextView.BufferType.SPANNABLE)
            distanceInputNumber.setText(trainingLogRow.distance.toString(),TextView.BufferType.SPANNABLE)

            val hashMap: HashMap<String,Int> = parseDurationStringToHashMap(trainingLogRow.durationOfLog.toString())
            numberPickerHour.value = hashMap["Hour"]!!
            numberPickerMinutes.value =hashMap["Minute"]!!
            numberPickerSeconds.value = hashMap["Second"]!!


            buttonDone.setOnClickListener{updateTrainingLogRow()}
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
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

            val action = AddTrainingLogDirections.actionAddTrainingLogToTrainingLogList()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateTrainingLogRow() {
        if (isEntryValid()) {
            viewModel.updateTrainingLogRow(                this.navigationArgs.logId,
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

        binding.timeButton.setOnClickListener{ setTime() }
        binding.dateButton.setOnClickListener{ setDate() }

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
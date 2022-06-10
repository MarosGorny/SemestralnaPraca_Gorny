package com.example.traininglog.gorny.maros

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.traininglog.gorny.maros.data.TrainingLogRow
import com.example.traininglog.gorny.maros.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.button.text = "A button"

        //tu by som mal naplnit arrayList
        val datumAktivityRun:Date = Date(2022, 11, 23)
        val casZaciatkuAktivityRun: kotlin.time.Duration = 15.hours + 23.minutes
        //druhy stlpec
        val casAktivityRun: kotlin.time.Duration = 48.minutes
        //stvrty stlpec
        val casMinPKm: kotlin.time.Duration = 6.minutes + 23.seconds

    }




}
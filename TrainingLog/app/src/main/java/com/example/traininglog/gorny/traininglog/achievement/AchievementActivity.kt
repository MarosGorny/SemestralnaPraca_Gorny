package com.example.traininglog.gorny.traininglog.achievement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.traininglog.gorny.traininglog.R

class AchievementActivity :AppCompatActivity() {

    private lateinit var navController: NavController
    //private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.achievements_activity)
    }
}
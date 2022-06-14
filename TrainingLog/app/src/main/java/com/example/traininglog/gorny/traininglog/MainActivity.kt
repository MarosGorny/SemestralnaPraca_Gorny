package com.example.traininglog.gorny.traininglog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.traininglog.gorny.traininglog.addActivity.*
import com.google.android.material.bottomnavigation.BottomNavigationView

const val TRAINING_LOG_ID = "trainingLog id"

class MainActivity : AppCompatActivity() {
    private val newTrainingLogActivityRequestCode = 1

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.trainingLogListFragment,R.id.trainingLogListFragment,R.id.trainingLogListFragment)
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }



}
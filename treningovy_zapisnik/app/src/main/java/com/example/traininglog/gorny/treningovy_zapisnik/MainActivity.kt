package com.example.traininglog.gorny.treningovy_zapisnik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Hlavna aktivita ktora sa stara o vytvorenie hlavneho layoutu,
 * v ktorom sa mozem pohybovat. Vytvori aj actionBar v ktorom sa moze pouzivatel
 * pohybovat medzi fragmentami
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Metoda sa zavola pri vytvoreni aktivity a nastavi
     * spodnu navigaciu a action bar kde nastavi list a achievementy
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Nastavi spodok navigacie s controllerom
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Nastavi ActionBar s navControllerom a 2 destinacie
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.trainingLogListFragment,R.id.runAchievementList)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    /**
     * Tato metoda je zavolna vzdy ked sa uzivatel bude chciet vratit spat
     * a vrati ho naspat na bar configuration
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
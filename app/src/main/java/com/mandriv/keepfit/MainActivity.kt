package com.mandriv.keepfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupViews()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupViews() {
        // Finding the Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment)
        // Setting Navigation Controller with the BottomNavigationView
        setupBottomNavigationBar(navController)
        // Setting Up ActionBar with Navigation Controller
        setupActionBar(navController)
    }

    private fun setupBottomNavigationBar(navController: NavController) {
        bottom_nav_view.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController) {
        // Setting Up ActionBar with Navigation Controller
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.today,
                R.id.goals,
                R.id.history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

}

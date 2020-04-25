package com.mandriv.keepfit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mandriv.ctnotifications.CTServiceActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CTServiceActivity("com.mandriv.keepfit") {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupViews()
            askForPermissions()
        } // Else, need to wait for onRestoreInstanceState
    }

    fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
    }

    fun createOrRetrieveNotificationChannel(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.notificationChannels.size > 0) {
                return notificationManager.notificationChannels[0].id
            }
            val channelId = "abc"
            val newChannel = NotificationChannel(channelId, "Main", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(newChannel)
            return channelId
        }
        return null
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

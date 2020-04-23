package com.mandriv.ctnotifications

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


abstract class CTServiceActivity(val activityPackageName: String): AppCompatActivity() {

    private val TAG = "CTServiceActivity"

    var ctService: ICTService? = null
    private var connection: CTServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCTService()
    }

    /** Called when the activity is about to be destroyed.  */
    override fun onDestroy() {
        super.onDestroy()
        releaseCTService()
    }

    inner class CTServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            ctService = ICTService.Stub.asInterface(boundService)
            Log.d(TAG, "onServiceConnected() connected")
            onCTServiceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            ctService = null
            Log.d(TAG, "onServiceDisconnected() disconnected")
            onCTServiceDisconnected()
        }
    }

    open fun onCTServiceConnected() {}
    open fun onCTServiceDisconnected() {}

    /** Binds this activity to the service.  */
    open fun initCTService() {
        connection = CTServiceConnection()
        val i = Intent()
        i.setClassName(activityPackageName, "com.mandriv.ctnotifications.CTService")
        val ret = bindService(i, connection!!, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "initService() bound with $ret")
    }

    /** Unbinds this activity from the service.  */
    open fun releaseCTService() {
        connection?.let { unbindService(it) }
        connection = null
        Log.d(TAG, "releaseService() unbound.")
    }
}
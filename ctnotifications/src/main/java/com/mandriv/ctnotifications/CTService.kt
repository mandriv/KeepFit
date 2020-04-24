package com.mandriv.ctnotifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.room.RoomDatabase
import com.mandriv.ctnotifications.data.ServiceDatabase

class CTService: Service() {
    private val binder = ICTServiceImpl(this)

    override fun onCreate() {
        super.onCreate()
        binder.database = ServiceDatabase.getInstance(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}
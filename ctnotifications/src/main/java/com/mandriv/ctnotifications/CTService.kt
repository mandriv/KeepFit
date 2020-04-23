package com.mandriv.ctnotifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CTService: Service() {
    private val binder = ICTServiceImpl(this)

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}
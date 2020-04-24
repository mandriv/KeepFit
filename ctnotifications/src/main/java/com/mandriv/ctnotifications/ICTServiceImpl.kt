package com.mandriv.ctnotifications

import android.content.Context
import android.os.RemoteException
import com.mandriv.ctnotifications.data.CustomNumericTrigger
import com.mandriv.ctnotifications.data.ServiceDatabase
import com.mandriv.ctnotifications.data.Trigger
import kotlinx.coroutines.coroutineScope

class ICTServiceImpl(private val ctx: Context): ICTService.Stub() {

    lateinit var database: ServiceDatabase

    @Throws(RemoteException::class)
    override fun getCurrentTriggers(): List<Trigger> {
        return database.triggerDao().getAll()
    }

    @Throws(RemoteException::class)
    override fun addTrigger(t: Trigger) {
        database.triggerDao().insert(t)
    }

}
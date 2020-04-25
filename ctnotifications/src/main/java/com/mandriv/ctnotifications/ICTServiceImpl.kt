package com.mandriv.ctnotifications

import android.os.RemoteException
import com.mandriv.ctnotifications.data.CustomNumericTrigger
import com.mandriv.ctnotifications.data.ServiceDatabase
import com.mandriv.ctnotifications.data.Trigger

class ICTServiceImpl : ICTService.Stub() {

    lateinit var database: ServiceDatabase

    @Throws(RemoteException::class)
    override fun getCurrentTriggers(): List<Trigger> {
        return database.triggerDao().getAll()
    }

    @Throws(RemoteException::class)
    override fun addTrigger(t: Trigger) {
        database.triggerDao().insert(t)
    }

    @Throws(RemoteException::class)
    override fun clearTriggers(appPackage: String) {
        database.triggerDao().clearAppTriggers(appPackage)
    }

    @Throws(RemoteException::class)
    override fun removeTrigger(triggerId: Int) {
        database.triggerDao().deleteTrigger(triggerId)
    }

    @Throws(RemoteException::class)
    override fun updateCustomData(appPackage: String, dataName: String, newValue: Int) {
        database.triggerDao().getAllByPackageName(appPackage).map { t ->
            val newCnts = t.customNumeric.map { cnt ->
                var newCnt = cnt
                if (newCnt.name.equals(dataName)) {
                    newCnt = CustomNumericTrigger(newCnt.name, newCnt.min, newCnt.max, newValue)
                }
                newCnt
            }
            Trigger(
                t.appPackage,
                t.nContentTitle,
                t.nContentText,
                t.nSmallIcon,
                t.nChannelId,
                t.nBadgeType,
                t.nCategory,
                t.nColor,
                t.nColorized,
                t.nContentInfo,
                t.nDefaults,
                t.nPriority,
                t.cTimeFrom,
                t.cTimeTo,
                t.weatherTempMin,
                t.weatherTempMax,
                t.weatherDesc,
                t.policy,
                newCnts,
                t.id
            )
        }.forEach { t -> database.triggerDao().update(t) }

    }

}
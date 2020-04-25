package com.mandriv.ctnotifications.contextproviders.location

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

object LocationRepository {

    suspend fun getLastLocation(ctx: Context): Location {
        val client = LocationServices.getFusedLocationProviderClient(ctx)
        return client.lastLocation.await()

    }

}
package com.mandriv.ctnotifications.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

class CustomNumericTrigger(
    val name: String,
    val min: Int? = null,
    val max: Int? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun toString(): String {
        return "$name-$min-$max"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(min)
        parcel.writeValue(max)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomNumericTrigger> {
        override fun createFromParcel(parcel: Parcel): CustomNumericTrigger {
            return CustomNumericTrigger(parcel)
        }

        override fun newArray(size: Int): Array<CustomNumericTrigger?> {
            return arrayOfNulls(size)
        }
    }
}

@Entity(tableName = "triggers")
data class Trigger(
    val appPackage: String,
    val nTitle: String,
    val nDesc: String,
    val cTimeFrom: String = "",
    val cTimeTo: String = "",
    val weatherTempMin: Int? = null,
    val weatherTempMax: Int? = null,
    val weatherDesc: String = "",
    val customNumeric: List<CustomNumericTrigger> = emptyList(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString().orEmpty(),
        parcel.createTypedArrayList(CustomNumericTrigger).orEmpty().toList(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appPackage)
        parcel.writeString(nTitle)
        parcel.writeString(nDesc)
        parcel.writeString(cTimeFrom)
        parcel.writeString(cTimeTo)
        parcel.writeValue(weatherTempMin)
        parcel.writeValue(weatherTempMax)
        parcel.writeString(weatherDesc)
        parcel.writeTypedList(customNumeric)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trigger> {
        override fun createFromParcel(parcel: Parcel): Trigger {
            return Trigger(parcel)
        }

        override fun newArray(size: Int): Array<Trigger?> {
            return arrayOfNulls(size)
        }
    }

}
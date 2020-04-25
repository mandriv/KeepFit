package com.mandriv.ctnotifications.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

class CustomNumericTrigger(
    val name: String,
    val min: Int? = null,
    val max: Int? = null,
    val currentValue: Int? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(min)
        parcel.writeValue(max)
        parcel.writeValue(currentValue)
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

    override fun toString(): String {
        return "$name-$min-$max-$currentValue"
    }
}

object TriggerPolicy {
    const val NO_POLICY = "NO_POLICY"
    const val DAILY = "DAILY"
    const val WEEKLY = "WEEKLY"
}

@Entity(tableName = "triggers")
data class Trigger(
    val appPackage: String,
    val nContentTitle: String,
    val nContentText: String,
    val nSmallIcon: Int,
    val nChannelId: String,
    val nBadgeType: Int? = null,
    val nCategory: String = "",
    val nColor: Int? = null,
    val nColorized: Boolean = false,
    val nContentInfo: String = "",
    val nDefaults: Int? = null,
    val nPriority: Int? = null,
    val cTimeFrom: String = "",
    val cTimeTo: String = "",
    val weatherTempMin: Int? = null,
    val weatherTempMax: Int? = null,
    val weatherDesc: String = "",
    val policy: String = TriggerPolicy.DAILY,
    val customNumeric: List<CustomNumericTrigger> = emptyList(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(), // app package
        parcel.readString().orEmpty(), // title
        parcel.readString().orEmpty(), // desc
        parcel.readValue(Int::class.java.classLoader) as Int, // small icon
        parcel.readString().orEmpty(), // channel id
        parcel.readValue(Int::class.java.classLoader) as? Int, // badge type
        parcel.readString().orEmpty(), // category
        parcel.readValue(Int::class.java.classLoader) as? Int, // color
        parcel.readBoolean(), // colorized
        parcel.readString().orEmpty(), // content info
        parcel.readValue(Int::class.java.classLoader) as? Int, // defaults
        parcel.readValue(Int::class.java.classLoader) as? Int, // priority
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.createTypedArrayList(CustomNumericTrigger).orEmpty().toList(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appPackage)
        parcel.writeString(nContentTitle)
        parcel.writeString(nContentText)
        parcel.writeValue(nSmallIcon)
        parcel.writeString(nChannelId)
        parcel.writeValue(nBadgeType)
        parcel.writeString(nCategory)
        parcel.writeValue(nColor)
        parcel.writeBoolean(nColorized)
        parcel.writeString(nContentInfo)
        parcel.writeValue(nDefaults)
        parcel.writeValue(nPriority)
        parcel.writeString(cTimeFrom)
        parcel.writeString(cTimeTo)
        parcel.writeValue(weatherTempMin)
        parcel.writeValue(weatherTempMax)
        parcel.writeString(weatherDesc)
        parcel.writeString(policy)
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
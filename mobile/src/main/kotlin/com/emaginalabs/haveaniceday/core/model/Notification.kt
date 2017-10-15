package com.emaginalabs.haveaniceday.core.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Notification(
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null,
        val title: String?,
        val message: String?,
        val photoUrl: String?,
        val createdAt: Long,
        val read: Boolean = false
        ): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte())

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notification> {
        override fun createFromParcel(parcel: Parcel): Notification {
            return Notification(parcel)
        }

        override fun newArray(size: Int): Array<Notification?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(photoUrl)
        parcel.writeLong(createdAt)
        parcel.writeByte(if (read) 1 else 0)
    }

}

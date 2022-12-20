package com.kirtan.shah.mycontacts.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize  //is for passing this object in budle to comunicate between fragments , Also implement Parcelable interface
class Contact(
    var name:String,
    var phone:String?,
    var contactid:String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0

}
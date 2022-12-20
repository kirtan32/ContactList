package com.kirtan.shah.mycontacts.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kirtan.shah.mycontacts.model.Contact

@Database(entities = arrayOf(Contact::class), version = 1, exportSchema = false)
abstract class ContactBookDataBase : RoomDatabase()
{

    companion object
    {
        fun get(context:Context):ContactBookDataBase
        {
            return Room.databaseBuilder(context,ContactBookDataBase::class.java,"contacts").build()
        }
    }

    abstract fun getContactDao():ContactBookDAO

}
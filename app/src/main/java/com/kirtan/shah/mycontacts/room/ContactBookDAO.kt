package com.kirtan.shah.mycontacts.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kirtan.shah.mycontacts.model.Contact


@Dao
interface ContactBookDAO {

    @Insert
    suspend fun insert(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM Contact")
    suspend fun getAllContacts():List<Contact>

    @Update
    suspend fun update(contact: Contact)

    @Query("DELETE FROM Contact")
    suspend fun deleteAllContacts()

}
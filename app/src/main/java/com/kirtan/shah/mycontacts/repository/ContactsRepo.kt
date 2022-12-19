package com.kirtan.shah.mycontacts.repository

import android.content.Context
import com.kirtan.shah.mycontacts.model.Contact
import com.kirtan.shah.mycontacts.room.ContactBookDataBase

class ContactsRepo {

    suspend fun insert(context: Context,contact: Contact)
    {
        ContactBookDataBase.get(context).getContactDao().insert(contact)
    }

    suspend fun delete(context: Context,contact: Contact)
    {
        ContactBookDataBase.get(context).getContactDao().delete(contact)
    }

    suspend fun update(context: Context,contact: Contact)
    {
        ContactBookDataBase.get(context).getContactDao().update(contact)
    }

    suspend fun getAllContacts(context: Context):List<Contact>
    {
        return ContactBookDataBase.get(context).getContactDao().getAllContacts()
    }

    suspend fun deleteAllContacts(context: Context)
    {
        ContactBookDataBase.get(context).getContactDao().deleteAllContacts()
    }


}
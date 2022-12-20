package com.kirtan.shah.mycontacts.contract

import android.content.ContentResolver
import android.content.Context
import com.kirtan.shah.mycontacts.ContactViewModel
import com.kirtan.shah.mycontacts.model.Contact

public interface ContactContracts
{
    interface Presenter{
        fun getContacts(contentResolver: ContentResolver,context: Context,viewModel: ContactViewModel)
        fun getListOfContacts(context: Context,viewModel: ContactViewModel)
        fun deleteAllContacts(context: Context,viewModel: ContactViewModel)
        fun getEditContacts(context: Context,contact: Contact,contentResolver: ContentResolver)
        fun updateContact(context: Context,viewModel: ContactViewModel,contact: Contact)
    }
}
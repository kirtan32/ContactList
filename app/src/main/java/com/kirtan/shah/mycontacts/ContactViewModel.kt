package com.kirtan.shah.mycontacts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirtan.shah.mycontacts.model.Contact
import com.kirtan.shah.mycontacts.repository.ContactsRepo
import kotlinx.coroutines.launch

class ContactViewModel :ViewModel()
{
    val repo = ContactsRepo()
    val list = MutableLiveData<List<Contact>>()

    fun insert(context: Context,contact: Contact)
    {
        viewModelScope.launch{
            repo.insert(context,contact)
        }

    }
    fun delete(context: Context, contact: Contact)
    {
        viewModelScope.launch {
            repo.delete(context,contact)
        }
    }
    fun deleteAll(context: Context)
    {
        viewModelScope.launch {
            repo.deleteAllContacts(context)
        }
    }
    fun update(context: Context,contact: Contact)
    {
        viewModelScope.launch {
            repo.update(context,contact)
        }
    }
    fun getAllNotebooks(context: Context)
    {
        viewModelScope.launch {
            list.value=repo.getAllContacts(context)
        }
    }
}
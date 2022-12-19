package com.kirtan.shah.mycontacts.presenter

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.kirtan.shah.mycontacts.ContactViewModel
import com.kirtan.shah.mycontacts.contract.ContactContracts
import com.kirtan.shah.mycontacts.model.Contact
import com.kirtan.shah.mycontacts.repository.ContactsRepo

class ContactPresenter : ContactContracts.Presenter
{
    @SuppressLint("Range")
    override fun getContacts(contentResolver: ContentResolver,context: Context,viewModel: ContactViewModel) {
        var stringBuilder : StringBuilder = StringBuilder()
        val contectresolver: ContentResolver = contentResolver
        val cursor: Cursor? =
            contectresolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cursor!!.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    var id: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                            ?: "ID Not Found"
                    var name: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            ?: "Name Not Found"

                    var hasPhoneNumber: Int =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()

                    if (hasPhoneNumber > 0) {
                        val cursor2 = contectresolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        while(cursor2!!.moveToNext())
                        {
                            val phoneNumber : String = cursor2.getString(cursor2.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                            stringBuilder.append("Contact : ").append(name).append(", Phone number: ").append(phoneNumber).append("\n");
                            val contact = Contact(name, phoneNumber)

                            context.let { viewModel.insert(it,contact) }
                        }
                        cursor2.close();
                    }
                } catch (ex: Exception) {
                    Toast.makeText(context, "Something Wrong:\n${ex.message}", Toast.LENGTH_LONG)
                        .show()
                    ex.printStackTrace()
                }
            }
        }
        cursor.close()
    }

    override fun getListOfContacts(context: Context, viewModel: ContactViewModel){
        context.let { viewModel.getAllNotebooks(it) }
    }

    override fun deleteAllContacts(context: Context, viewModel: ContactViewModel) {
        context.let { viewModel.deleteAll(it) }
    }

}
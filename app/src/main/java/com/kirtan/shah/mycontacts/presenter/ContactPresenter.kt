package com.kirtan.shah.mycontacts.presenter

import android.R
import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentValues
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
                            val phoneId : String = cursor2.getString(cursor2.getColumnIndex(ContactsContract.Data.CONTACT_ID));

                            stringBuilder.append("Contact : ").append(name).append(", Phone number: ").append(phoneNumber).append("\n");
                            val contact = Contact(name, phoneNumber,phoneId)

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

    override fun getEditContacts(context: Context, contact: Contact,contentResolver: ContentResolver) {
        val contectresolver: ContentResolver = contentResolver
        val cursor: Cursor? =
            contectresolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        try {

            if (cursor!!.moveToFirst()) {
                val rawContactId = cursor.getString(0)
                val ops = ArrayList<ContentProviderOperation>()
                val contentValues = ContentValues()
                contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                contentValues
                    .put(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                contentValues.put(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    R.attr.phoneNumber
                )
                contentValues.put(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                )
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValues(contentValues).build()
                )
                val contactId: String = contact.contactid//contactUri.getLastPathSegment()
                ops.add(
                    ContentProviderOperation
                        .newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            ContactsContract.Data.CONTACT_ID
                                    + "=? AND "
                                    + ContactsContract.Data.MIMETYPE
                                    + "=?", arrayOf(
                                contactId,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                            )
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            contact.name
                        ).build()
                )
                contentResolver.applyBatch(
                    ContactsContract.AUTHORITY, ops
                )
            }

        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    override fun updateContact(context: Context, viewModel: ContactViewModel, contact: Contact) {
        context.let { viewModel.update(it,contact) }
    }

}
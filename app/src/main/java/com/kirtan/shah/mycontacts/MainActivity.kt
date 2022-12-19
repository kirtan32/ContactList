package com.kirtan.shah.mycontacts

import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.WRITE_CONTACTS
import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kirtan.shah.mycontacts.adapters.ClickHandler
import com.kirtan.shah.mycontacts.adapters.MyAdapter
import com.kirtan.shah.mycontacts.loginsignup.LoginSignup
import com.kirtan.shah.mycontacts.model.Contact
import com.kirtan.shah.mycontacts.presenter.ContactPresenter


class MainActivity : AppCompatActivity(),ClickHandler {

    lateinit var helloworldtextview: TextView
    lateinit var signout : MaterialButton
    companion object{
        lateinit var presenter : ContactPresenter
    }
    lateinit var viewModels: ContactViewModel
    lateinit var recycler : RecyclerView
    var adapter = MyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InitElements()
        if(checkPermission())
        {
            Toast.makeText(this,"Contact Read Permission Granted",Toast.LENGTH_SHORT).show()
            recycler=findViewById<RecyclerView>(R.id.contactRecyclerView)
            this.let {
                presenter.deleteAllContacts(it,viewModels)
                presenter.getContacts(contentResolver,it,viewModels)
                setRecyclerView()
            }
        }
        else
        {
            Toast.makeText(this,"Sorry! Without this permission App will not functioned",Toast.LENGTH_SHORT).show()
            requestPermission()
        }
        //getContacts()
        signout.setOnClickListener {
            LoginSignup.presenter.DoSignOut(this)
            doLogOut()
            val myIntent = Intent(
                this,
                LoginSignup::class.java
            )
            Toast.makeText(this, "Signed Out", Toast.LENGTH_LONG).show()
            startActivity(myIntent)
            finish()
        }

    }

    fun setRecyclerView()
    {
        presenter.getListOfContacts(this, viewModels)
        viewModels.list.observe(this , Observer {

            adapter.setContentList(it)
            recycler.also { recycler ->
                recycler.adapter = adapter
            }

        })
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, READ_CONTACTS)
        val result1= ContextCompat.checkSelfPermission(applicationContext, WRITE_CONTACTS)
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(READ_CONTACTS),
//            200
//        )
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(WRITE_CONTACTS),
//            201
//        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_CONTACTS, WRITE_CONTACTS),
            200
        )
    }

    fun InitElements()
    {
        helloworldtextview=findViewById<TextView>(R.id.sampletextview)
        viewModels = ViewModelProvider(this)[ContactViewModel::class.java]
        signout = findViewById<MaterialButton>(R.id.signout)
        presenter = ContactPresenter()
    }

    fun doLogOut()
    {
        val pref = this.getSharedPreferences("contactPrefs", AppCompatActivity.MODE_PRIVATE )
        val editor = pref.edit()
        editor.putBoolean("isLogin", false)
        editor.putString("email","")
        editor.apply()
    }

    @SuppressLint("Range")
    private fun getContacts() {
        var stringBuilder : StringBuilder = StringBuilder()
        val contectresolver: ContentResolver = contentResolver
        val cursor: Cursor? =
            contectresolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cursor!!.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    var id: String =
                        cursor?.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                            ?: "ID Not Found"
                    var name: String =
                        cursor?.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
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
                            val phoneNumber : String = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //val phoneId : String = cursor2.getString(cursor2.getColumnIndex(ContactsContract.Data.CONTACT_ID));

                            stringBuilder.append("Contact : ").append(name).append(", Phone number: ").append(phoneNumber).append("\n");

                        }
                        cursor2.close();
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this, "Something Wrong:\n${ex.message}", Toast.LENGTH_LONG)
                        .show()
                    ex.printStackTrace()
                }
            }
        }
        cursor.close()
        helloworldtextview.text=stringBuilder.toString()
    }

    override fun handleClick(contact: Contact) {

        var dialogStart = Dialog(this)
        dialogStart.show()
        dialogStart.setCanceledOnTouchOutside(true)
        dialogStart.setContentView(R.layout.edit_contact)
        val firstname : EditText = dialogStart.findViewById(R.id.firstnameedittext)
        val lastname : EditText = dialogStart.findViewById(R.id.lastnameedittext)
        val phoneno : TextView = dialogStart.findViewById(R.id.phonenotextview)
        phoneno.text = contact.phone
        var editContact : MaterialButton = dialogStart.findViewById(R.id.editcontactbutton)
        val names : List<String> = contact.name.toString().split(" ", limit = 2)
        if(names.size>1)
        {
            firstname.setText(names[0])
            lastname.setText(names[1])
        }
        else
        {
            firstname.setText(names[0])
            lastname.setText("")
        }
        editContact.setOnClickListener {

            this.let {
                contact.name = firstname.text.toString()+" "+lastname.text.toString()
                viewModels.update(this,contact)
                editContact(contact)
                setRecyclerView()
            }
            dialogStart.dismiss()
        }
    }

    fun editContact(contact: Contact)
    {
        var contentProviderOperations:ArrayList<ContentProviderOperation> = ArrayList<ContentProviderOperation>()

//        contentProviderOperations.add(ContentProviderOperation
//            .newUpdate(ContactsContract.Data.CONTENT_URI)
//            .withSelection(ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ?",
//                arrayOf(name,
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
//            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
//                phone)
//            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//            .build());
        //-----------------
//        contentProviderOperations.add(ContentProviderOperation
//            .newUpdate(ContactsContract.Data.CONTENT_URI)
//            .withSelection(ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ?",
//                arrayOf(phone,
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
//            .withValue(ContactsContract.Data.DISPLAY_NAME,
//                name)
//            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//            .build());
        //-----------------
        contentProviderOperations.add(ContentProviderOperation
            .newUpdate(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                ContactsContract.Data.CONTACT_ID
                        + "=? AND "
                        + ContactsContract.Data.MIMETYPE
                        + "=?", arrayOf(
                    contact.phone,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
            )
            .withValue(
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                contact.name
            ).build()
        )

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY,contentProviderOperations);
            Toast.makeText(this, "Contact Updated Successfully", Toast.LENGTH_LONG).show();
        }
        catch (ex:Exception)
        {
            Toast.makeText(this,"Error in Updating Contact : ${ex.message}",Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }
    }

    fun getEditContacts(contact: Contact)
    {
        val contectresolver: ContentResolver = contentResolver
        val cursor: Cursor? =
            contectresolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
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
                        phoneNumber
                    )
                    contentValues.put(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                    )
                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValues(contentValues).build()
                    )
                    val contactId: String = ContactsContract.Contacts.CONTENT_URI.getLastPathSegment().toString()
                    ops.add(
                        ContentProviderOperation
                            .newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(
                                ContactsContract.Data.CONTACT_ID
                                        + "=? AND "
                                        + ContactsContract.Data.MIMETYPE
                                        + "=?", arrayOf(
                                    contact.phone,
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
                cursor.close()
            }

        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }

    }

}
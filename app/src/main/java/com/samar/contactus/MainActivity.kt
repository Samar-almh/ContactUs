package com.samar.contactus

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import androidx.core.app.ActivityCompat

private const val REQUEST_PHONE = 0
private lateinit var github: ImageView
private lateinit var phone: ImageView
private lateinit var location: ImageView
private lateinit var email: ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        github = findViewById(R.id.imageView3)
        phone = findViewById(R.id.imageView4)
        location = findViewById(R.id.imageView5)
        email = findViewById(R.id.imageView6)

        github.setOnClickListener {
            val githubIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("https://github.com/samar.almh")

            }
            if (githubIntent.resolveActivity(packageManager) != null) {
                startActivity(githubIntent)
            }
        }
        location.setOnClickListener {
            val locationIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("geo:2.575673,44.012508")

            }
            if (locationIntent.resolveActivity(packageManager) != null) {
                startActivity(locationIntent)
            }

        }
        email.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("samar.almh@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                putExtra(Intent.EXTRA_TEXT, "Email message text")
            }
            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(sendIntent)
            }

        }
        phone.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_PHONE
                    )
                } else {
                    if(contactPhone(this@MainActivity ,"775178911")==false) {
                        addPhone("SAMAR", "samar.almh@gmail.com", "775178911")
                    }else{
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:" + Uri.encode("775178911"))
                        startActivity(callIntent)
                    }

                }
            }
    }

    fun contactPhone(context: Context, number: String?): Boolean {
        return if (number != null) {
            val cr: ContentResolver = context.getContentResolver()
            val curContacts: Cursor? =
                cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            while (curContacts!!.moveToNext()) {
                val contactNumber: String =
                    curContacts.getString(curContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                if (number == contactNumber) {
                    return true
                }
            }
            false
        } else {
            false
        }
    }
    fun addPhone(contact_name:String,contact_email:String, contact_phone:String){

        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(
                ContactsContract.Intents.Insert.NAME,
                contact_name
            )
            putExtra(
                ContactsContract.Intents.Insert.EMAIL,
                contact_email
            )
            putExtra(
                ContactsContract.Intents.Insert.EMAIL_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_WORK
            )
            putExtra(
                ContactsContract.Intents.Insert.PHONE,
                contact_phone
            )
            putExtra(
                ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK
            )
        }
        startActivity(intent)
    }
}
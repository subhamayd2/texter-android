package com.aztechcorps.texter.Services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.aztechcorps.texter.Models.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Subhamay on 03-Apr-18.
 */

public class SmsService {
    private Context context;

    public SmsService(Context context) {
        this.context = context;
    }

    public JSONObject getAllContacts() {

        List<Contact> contacts = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("testing_2", "ID: " + id);
                        Log.i("testing_2", "Name: " + name);
                        Log.i("testing_2", "Phone Number: " + phoneNo);
                        contacts.add(new Contact(id, name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        JSONObject contactsJsonObj = new JSONObject();
        try {
            JSONArray contactsJsonArray = new JSONArray();
            for(Contact c: contacts) {
                contactsJsonArray.put(c);
            }
            contactsJsonObj.put("Contacts", contactsJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contactsJsonObj;
    }

    public void getAllSms() {
        Cursor cur = context.getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);

        if (cur.moveToFirst()) { /* false = no sms */
            do {
                String msgInfo = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    msgInfo += " " + cur.getColumnName(i) + ":" + cur.getString(i);
                }

                //Toast.makeText(context, msgInfo, Toast.LENGTH_SHORT).show();
                Log.d("testing", msgInfo);
            } while (cur.moveToNext());
        }
    }
}

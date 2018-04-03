package com.aztechcorps.texter.Activities;

/**
 * Created by Subhamay on 03-Apr-18.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aztechcorps.texter.R;
import com.aztechcorps.texter.Services.SmsService;
import com.aztechcorps.texter.Services.SocketService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmsService smsService = new SmsService(this);
        smsService.getAllSms();
        smsService.getAllContacts();

        SocketService.getInstance();
    }
}

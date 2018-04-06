package com.aztechcorps.texter.Activities;

/**
 * Created by Subhamay on 03-Apr-18.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aztechcorps.texter.QRCodeReader.BarcodeCaptureActivity;
import com.aztechcorps.texter.R;
import com.aztechcorps.texter.Services.SharedPreferencesHandler;
import com.aztechcorps.texter.Services.SmsService;
import com.aztechcorps.texter.Services.SocketService;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;


public class MainActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private String BarcodeStatus;
    private String BarcodeValue;

    private Button btnQRScan;

    SmsService smsService;
    SharedPreferencesHandler sharedPreferencesHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQRScan = (Button) findViewById(R.id.btnQRScan);

        sharedPreferencesHandler = new SharedPreferencesHandler(this);

        if(!sharedPreferencesHandler.check()) {
            sharedPreferencesHandler.init();
        }

        btnQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        smsService = new SmsService(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //statusMessage.setText(R.string.barcode_success);
                    BarcodeStatus = getResources().getString(R.string.barcode_success);
                    //barcodeValue.setText(barcode.displayValue);
                    BarcodeValue = barcode.displayValue;
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    renderUI();
                } else {
                    BarcodeStatus = getResources().getString(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                BarcodeStatus = String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void renderUI() {
        SocketService socketService = SocketService.getInstance();
        socketService.initializeSocket(getApplicationContext());
        socketService.connect(BarcodeValue);
    }
}

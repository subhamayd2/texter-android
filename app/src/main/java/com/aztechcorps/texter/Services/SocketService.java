package com.aztechcorps.texter.Services;

import android.content.Context;
import android.util.Log;

import com.aztechcorps.texter.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Subhamay on 03-Apr-18.
 */

public class SocketService {
    private static SocketService obj;
    private static Socket socket;
    private Context context;
    private String barcodeValue;

    private static final String TAG = "SocketTest";

    private SocketService() {
        try {
            IO.Options opts = new IO.Options();
            opts.port = 3000;
            opts.query = "clientType=" + AppConstants.CLIENT_MOBILE;
            socket = IO.socket("http://192.168.0.100:3000", opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSocket(final Context context) {
        this.context = context;
        Log.d(TAG, "initializeSocket: asdsad");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                //socket.emit("foo", "hi");
                //socket.disconnect();
                Log.d(TAG, "Connected");

                sendMobileClientDetails();

            }

        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, args[0].toString());
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d(TAG, "Disconnected");
            }

        });

        listenSocketEvents();
    }

    private void listenSocketEvents() {
        socket.on(AppConstants.SEND_ALL_CONTACTS_EVENT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                sendAllContacts();
            }
        });
    }

    public static SocketService getInstance() {
        if (obj == null) {
            obj = new SocketService();
        }
        Log.d(TAG, "getInstance: created");
        return obj;
    }

    public Socket connect(String barcodeValue) {
        this.barcodeValue = barcodeValue;
        return socket.connect();
    }

    public static Socket getSocket() {
        return socket;
    }

    private void sendMobileClientDetails() {
        SharedPreferencesHandler sph = new SharedPreferencesHandler(context);
        JSONObject args = new JSONObject();
        try {
            args.put(AppConstants.MOBILE_TOKEN, sph.getToken());
            args.put(AppConstants.WEB_TOKEN, barcodeValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(AppConstants.SEND_MOBILE_CLIENT_INFO_EVENT, args);
    }

    private void sendAllContacts() {
        SmsService smsService = new SmsService(context);
        JSONObject contactsJsonObj = smsService.getAllContacts();
        socket.emit(AppConstants.SEND_ALL_CONTACTS_EVENT, contactsJsonObj);
    }



}

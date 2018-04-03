package com.aztechcorps.texter.Services;

import android.util.Log;

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

    private static final String TAG = "SocketTest";

    private SocketService() {
    }

    private void initializeSocket() {
        Log.d(TAG, "initializeSocket: asdsad");
        try {
            IO.Options opts = new IO.Options();
            opts.port = 3000;
            socket = IO.socket("http://192.168.0.100:3000", opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //socket.emit("foo", "hi");
                    //socket.disconnect();
                    Log.d(TAG, "Connected");
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Event: " + args[0]);
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
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static SocketService getInstance() {
        if (obj == null) {
            obj = new SocketService();
            obj.initializeSocket();
        }
        Log.d(TAG, "getInstance: created");
        return obj;
    }

    public static Socket getSocket() {
        return socket;
    }

}

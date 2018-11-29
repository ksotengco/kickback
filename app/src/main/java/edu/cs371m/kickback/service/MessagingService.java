package edu.cs371m.kickback.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Message", remoteMessage.getData().get("invite"));
    }

    @Override
    public void onNewToken(String s) {
        Log.d("newToken", "Token: " + s);
    }
}

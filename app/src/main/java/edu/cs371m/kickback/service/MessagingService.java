package edu.cs371m.kickback.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        Log.d("newToken", "Token: " + s);
    }
}

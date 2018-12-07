package edu.cs371m.kickback.service;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.cs371m.kickback.R;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("messageCreate", "create");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MessageRECEIVEDa", remoteMessage.getData().get("invite"));
        Log.d("NOTIFICATION__", remoteMessage.getNotification().getBody());
        String eventID = remoteMessage.getData().get("invite");


//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "some id")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("You have a new invite!")
//                .setContentText(eventID)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    @Override
    public void onNewToken(String s) {
        Log.d("newToken", "Token: " + s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}

package edu.cs371m.kickback.service;

import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import edu.cs371m.kickback.R;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("messageCreate", "create");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MessageRECEIVEDa", remoteMessage.getData().get("eventId"));
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> updateToken = new HashMap<>();
        updateToken.put("deviceToken", s);
        if (user != null) {
            Database.getInstance().db
                    .collection("profiles")
                    .document(user.getUid())
                    .set(updateToken)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("onNewToken", "onComplete: device token updated");
                            } else {
                                Log.d("onNewToken", "onComplete: " + task.getException().toString());
                            }
                        }
                    });

        }
        Log.d("newToken", "Token: " + s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}

package edu.cs371m.kickback.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.model.Profile;

public class Database {
    public final FirebaseFirestore db;

    private static final Database ourInstance = new Database();
    private static WaitForDataQuery callback;

    public static Database getInstance() {
        return ourInstance;
    }

    public static void setCallback(WaitForDataQuery dataCallback) {
        callback = dataCallback;
    }

    private Database() { db = FirebaseFirestore.getInstance(); }

    public void addProfile(final FirebaseUser profile, Bundle logInfo) {
        final Profile newProfile = new Profile(profile, logInfo);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(profile.getUid(), newProfile);

        final DocumentReference docRef = db.collection("profiles").document(profile.getUid());

        docRef.set(newProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("onComplete", "happens");
                            Map<String, Object> tempMap = new HashMap<String, Object>();
                            tempMap.put("viewed", true);
                            tempMap.put("marked", false);

                            db.collection("profiles").document(profile.getUid())
                                    .collection("invites")
                                    .document("dummy")
                                    .set(tempMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                callback.onProfileReady(newProfile);
                                            }
                                        }
                                    });
                        } else {
                            Log.d("ADD PROFILE", "onFailure: " + task.getException().getMessage());
                        }
                    }
                });

    }

    public void addInvite (String uID, String eventID) {
        Map<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("viewed", false);
        tempMap.put("marked", false);

        db.collection("profiles/" + uID + "/invites")
                .document(eventID)
                .set(tempMap);
    }

    public void getProfile(String uID) {
        db.collection("profiles")
                .whereEqualTo("id", uID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        callback.onProfileReady(queryDocumentSnapshots.toObjects(Profile.class).get(0));
                    }
                });
    }

    public void addEvent(final Event newEvent) {
        db.collection("events")
                .document(newEvent.getEventId())
                .set(newEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onEventReady(newEvent);
                    }
                });
    }
}

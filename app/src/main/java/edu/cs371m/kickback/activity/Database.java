package edu.cs371m.kickback.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void addProfile(FirebaseUser profile, Bundle logInfo) {
        final Profile newProfile = new Profile(profile, logInfo);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(profile.getUid(), newProfile);
        db.collection("profiles").document(profile.getUid()).set(newProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onProfileReady(newProfile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ADD PROFILE", "onFailure: " + e.toString());
                    }
                 });
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

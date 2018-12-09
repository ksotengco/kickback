package edu.cs371m.kickback.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.cs371m.kickback.listener.OnAddEventListener;
import edu.cs371m.kickback.listener.OnAddProfileListener;
import edu.cs371m.kickback.listener.OnGetProfileListener;
import edu.cs371m.kickback.listener.OnGetProfilesListener;
import edu.cs371m.kickback.listener.OnProfileSignOut;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.model.Invite;
import edu.cs371m.kickback.model.Profile;

public class Database {
    public FirebaseFirestore db;
    private static Database database;
    public enum EventUpdates {
        ACCEPTED,
        DECLINED
    }

    private Database() { db = FirebaseFirestore.getInstance(); }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public void addProfile(final Profile newProfile, final OnAddProfileListener callback) {
        db.collection("profiles")
                .document(newProfile.getId())
                .set(newProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onAddProfile(newProfile);
                        } else {
                            Log.d("ADD PROFILE", "onFailure: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getProfile(String uID, final OnGetProfileListener callback) {
        db.collection("profiles")
                .whereEqualTo("id", uID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        callback.onGetProfile(queryDocumentSnapshots.toObjects(Profile.class).get(0));
                    }
                });
    }

    public void signInProfile(final String uId, final OnGetProfileListener callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("active", true);
        db.collection("profiles")
                .document(uId)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("profiles")
                                .document(uId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        callback.onGetProfile(documentSnapshot.toObject(Profile.class));
                                    }
                                });
                    }
                });
    }

    public void signOutProfile(final String uId, final OnProfileSignOut callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("active", false);
        db.collection("profiles")
                .document(uId)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onProfileSignout(uId);
                    }
                });
    }

    public void getProfiles(final OnGetProfilesListener callback) {
        db.collection("profiles").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("COMPLETE", "TASK: " + task.isSuccessful());
                }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        callback.onGetProfiles((ArrayList<Profile>) queryDocumentSnapshots.toObjects(Profile.class));
                    }
                });
    }

    public void addEvent(final Event newEvent, final OnAddEventListener callback) {
        db.collection("events")
                .document(newEvent.getEventId())
                .set(newEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onAddEvent(newEvent);
                    }
                });
    }

    public void addInvite (String uID, String eventID) {
        db.collection("profiles/" + uID + "/invites")
                .document(eventID)
                .set(new Invite(eventID));
    }

    public void viewInvite (String uID, String eventID) {
        db.collection("profiles/" + uID + "/invites")
                .document(eventID)
                .update("viewed", true);
    }

    public void removeInvite (String uID, String eventID) {
        db.collection("profiles/" + uID + "/invites")
                .document(eventID)
                .delete();
    }

    public void inviteAccept (String eventID, EventUpdates update) {
        db.collection("events")
                .document(eventID)
                .update("pending",
                        FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getUid()));

        removeInvite(FirebaseAuth.getInstance().getCurrentUser().getUid(), eventID);

        if (update == EventUpdates.ACCEPTED) {
            db.collection("events")
                    .document(eventID)
                    .update("attendees",
                            FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        }
    }
}

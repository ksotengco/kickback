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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.cs371m.kickback.model.Profile;

public class Database {
    private final FirebaseFirestore db;

    private static final Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() { db = FirebaseFirestore.getInstance(); }

    public void addProfile(final waitForProfile callback, FirebaseUser profile, Bundle logInfo) {
        final Profile newProfile = new Profile(profile, logInfo);

        db.collection("profiles")
                .add(newProfile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Creating Profile...", "Success!");
                        callback.onProfileReady(newProfile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Creating Profile...", e.getMessage());
                    }
                });
    }

    public void getProfile(final waitForProfile callback, String uID) {
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
}

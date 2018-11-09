package edu.cs371m.kickback.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.cs371m.kickback.model.Profile;

public class Database {
    private final FirebaseFirestore db;

    private static final Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        db = FirebaseFirestore.getInstance();
    }

    public Profile addProfile(FirebaseUser profile, Bundle logInfo) {
        Profile newProfile = new Profile(profile, logInfo);

        db.collection("profiles")
                .add(newProfile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Creating Profile...", "Success!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Creating Profile...", e.getMessage());
                    }
                });

        return newProfile;
    }

    public Profile getProfile(String uID) {
        return null;
    }
}

package edu.cs371m.kickback.model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Profile {
    private String firstName;
    private String lastName;
    private String id;
    private String email;

    private Photo profilePicture;
    private ArrayList<String> hosting;
    private ArrayList<String> attending;
    private ArrayList<String> invites;

    private int totalRating;
    private int reviewCount;

    public Profile(String id) {
        // query DB for certain user information
    }

    public Profile(FirebaseUser profile, Bundle logInfo) {
        this.id = profile.getUid();
        this.email = profile.getEmail();

        this.firstName = logInfo.getString("firstName");
        this.lastName  = logInfo.getString("lastName");

        this.hosting = new ArrayList<String>();
        this.attending = new ArrayList<String>();
        this.invites = new ArrayList<String>();

        totalRating = 0;
        reviewCount = 0;
    }

    public static void addProfile(FirebaseUser profile, Bundle logInfo) {
        Profile newProfile = new Profile(profile, logInfo);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    }

    public static Profile getProfile(String uID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Photo getProfilePicture() {
        return profilePicture;
    }

    public ArrayList<String> getHosting() {
        return hosting;
    }

    public ArrayList<String> getAttending() {
        return attending;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }
}

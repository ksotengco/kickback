package edu.cs371m.kickback.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
    private String deviceToken;
    private boolean active;

    private Photo profilePicture;

    private int totalRating;
    private int reviewCount;

    public Profile() {}

    public Profile(String id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;

        totalRating = 0;
        reviewCount = 0;
    }

    // all the getters for database population (I assume)
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

    public boolean isActive() {
        return active;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Photo getProfilePicture() {
        return profilePicture;
    }

    public int getTotalRating() {
        return totalRating;
    }
    public int getReviewCount() {
        return reviewCount;
    }
}

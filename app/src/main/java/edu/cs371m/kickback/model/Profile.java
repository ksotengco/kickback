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
    private String profilePicUrl;
    private boolean active;

    public Profile() {}

    public Profile(String id, String email, String firstName, String lastName, String profilePicUrl) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicUrl = profilePicUrl;
    }

    public Bundle bundleData() {
        Bundle userInfo = new Bundle();
        userInfo.putString("id", id);
        userInfo.putString("firstName", firstName);
        userInfo.putString("lastName", lastName);
        userInfo.putString("email", email);
        userInfo.putString("picUrl", profilePicUrl);
        return userInfo;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}

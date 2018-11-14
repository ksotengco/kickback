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

    private Photo profilePicture;

    private int totalRating;
    private int reviewCount;

    public Profile() {}

    // creating a new user, populating it with email, name, id
    public Profile(FirebaseUser profile, Bundle logInfo) {
        this.id = profile.getUid();
        this.email = profile.getEmail();

        this.firstName = logInfo.getString("firstName");
        this.lastName  = logInfo.getString("lastName");

        //this.profilePicture = new Photo();

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

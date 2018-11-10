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

public class Profile implements Parcelable {
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

    // http://www.vogella.com/tutorials/AndroidParcelable/article.html
    // WITCHEL also might be from a flipped class but I don't remember exact one
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray (int size) {
            return new Profile[size];
        }
    };

    // default no-argument constructor; apparently it's needed or else failure
    public Profile() {}
    public Profile(String id) {
        // query DB for certain user information
    }

    public Profile(Parcel parcel) {
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.id = parcel.readString();
        this.email = parcel.readString();

        // TODO: figure out how to put Photo object in a Parcel

        parcel.readList(this.hosting, null);
        parcel.readList(this.attending, null);
        parcel.readList(this.invites, null);

        this.totalRating = parcel.readInt();
        this.reviewCount = parcel.readInt();
    }

    // turns data into parcel for parcelables
    // Note: I feel like I saw this in a FC but I don't remember where
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeString(this.id);
        parcel.writeString(this.email);

        // TODO: figure out how to put Photo object in a Parcel

        parcel.writeList(this.hosting);
        parcel.writeList(this.attending);
        parcel.writeList(this.invites);

        parcel.writeInt(totalRating);
        parcel.writeInt(reviewCount);
    }

    // using default from Docs
    @Override
    public int describeContents() {
        return 0;
    }

    public Profile(FirebaseUser profile, Bundle logInfo) {
        this.id = profile.getUid();
        this.email = profile.getEmail();

        this.firstName = logInfo.getString("firstName");
        this.lastName  = logInfo.getString("lastName");

        //this.profilePicture = new Photo();

        this.hosting = new ArrayList<String>();
        this.attending = new ArrayList<String>();
        this.invites = new ArrayList<String>();

        totalRating = 0;
        reviewCount = 0;
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

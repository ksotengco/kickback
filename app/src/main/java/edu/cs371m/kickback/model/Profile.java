package edu.cs371m.kickback.model;

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
}

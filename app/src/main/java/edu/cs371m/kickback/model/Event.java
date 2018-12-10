package edu.cs371m.kickback.model;

import android.os.Bundle;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.UUID;

public class Event {

    private String eventName;
    private String hostName;
    private String description;
    private String hostId;
    private String eventId;
    private String photoId;
    // TODO: Add time/date and location
    // TODO: ADd creation timestamp

    private String date;
    private ArrayList<String> location;

    private GeoPoint geolocation;

    private ArrayList<String> attendees;
    private ArrayList<String> pending;

    public Event() {}

    public Event(Bundle eventInfo) {
        this.eventName = eventInfo.getString("eventName");
        this.description = eventInfo.getString("description");
        this.hostId = eventInfo.getString("hostId");
        this.hostName = eventInfo.getString("hostName");
        this.eventId = UUID.randomUUID().toString();
        this.photoId = null; // TODO: change this

        this.date = eventInfo.getString("date");
        this.location = eventInfo.getStringArrayList("location");

        double latitude = eventInfo.getDoubleArray("geolocation")[0];
        double longitude = eventInfo.getDoubleArray("geolocation")[1];

        this.geolocation = new GeoPoint(latitude, longitude);

        /*this.latitude = eventInfo.getDouble("latitude");
        this.longitude = eventInfo.getDouble("longitude");*/
        //this.time = eventInfo.getIntegerArrayList("time");

        this.attendees = new ArrayList<String>();
        this.attendees.add(hostId);
        this.pending = eventInfo.getStringArrayList("pending");
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public ArrayList<String> getPending() {
        return pending;
    }

    public String getDescription() {
        return description;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostId() {
        return hostId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public GeoPoint getGeolocation() {
        return geolocation;
    }

    /*public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }*/

    /*public ArrayList<Integer> getDate() {
        return date;
    }

    public ArrayList<Integer> getTime() {
        return time;
    }*/

    public String toString() {
        String result = "Name: " + getEventName();
        return result;
    }
}

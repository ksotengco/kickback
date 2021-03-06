package edu.cs371m.kickback.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Appitivty;
import edu.cs371m.kickback.activity.MainActivity;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.page.creatingEvents.CreateEvent;
import edu.cs371m.kickback.page.searching.SearchResults;
import edu.cs371m.kickback.page.userEvents.MyEvents;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class HomePage extends Fragment {

    private RecyclerView recyclerView;
    private SearchFirestoreAdapter adapter;

    private ProgressBar progressBar;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private final double DISTANCE = 10.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page, container, false);

        progressBar = v.findViewById(R.id.progressBar);

        getActivity().requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mLocationRequest = LocationRequest.create();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    queryLocation(locationResult.getLastLocation());
                } else {
                    Log.d("getLastLocation", "Location is null.");
                }
            }
        };

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } catch (SecurityException e) {
            Log.d("HomePage", e.getLocalizedMessage());
        }

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    private void queryLocation(Location location) {
        try {
            Log.d("onLocationChanged", location.getLatitude() + " " + location.getLongitude());
            double lowerLat = location.getLatitude() - (0.0144927536231884 * DISTANCE);
            double lowerLng = location.getLongitude() - (0.0181818181818182 * DISTANCE);

            double upperLat = location.getLatitude() + (0.0144927536231884 * DISTANCE);
            double upperLng = location.getLongitude() + (0.0181818181818182 * DISTANCE);

            GeoPoint lowGeo   = new GeoPoint(lowerLat, lowerLng);
            GeoPoint upperGeo = new GeoPoint(upperLat, upperLng);

            Query q = Database.getInstance().db.collection("events")
                    .whereGreaterThanOrEqualTo("geolocation", lowGeo)
                    .whereLessThanOrEqualTo("geolocation", upperGeo);

            FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                    .setQuery(q, Event.class)
                    .build();

            if (adapter != null) {
                adapter.stopListening();
            }

            progressBar.setVisibility(View.GONE);

            adapter = new SearchFirestoreAdapter(options);
            adapter.startListening();
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);


        } catch (SecurityException e) {
            Log.d("onLocationChanged", e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }
}

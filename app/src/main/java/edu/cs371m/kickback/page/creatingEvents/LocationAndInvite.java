package edu.cs371m.kickback.page.creatingEvents;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.service.StateAbbr;

public class LocationAndInvite extends Fragment implements OnMapReadyCallback {

    OnButtonPressed cb;

    private EditText location;
    private EditText invites;

    private Button locationButton;
    private Button inviteButton;

    private Button createButton;
    private Button prevButton;

    private final ArrayList<String> pending = new ArrayList<>();
    private final ArrayList<String> locationArr = new ArrayList<>();

    private Geocoder g;

    private GoogleMap gmap;
    private MarkerOptions markerOptions;

    private float defaultZoom = 15.0f;


    // GoogleMaps code (some of it) taken from FC5
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_and_invite, container, false);

        cb = (OnButtonPressed) getActivity();

        g = new Geocoder(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerOptions = new MarkerOptions();



        location = (EditText) v.findViewById(R.id.editLocation);
        invites  = (EditText) v.findViewById(R.id.editInvites);

        locationButton = (Button) v.findViewById(R.id.locationButton);
        inviteButton   = (Button) v.findViewById(R.id.inviteButton);

        createButton     = (Button) v.findViewById(R.id.create_button);
        prevButton     = (Button) v.findViewById(R.id.prev_button);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(invites.getText().toString())) {
                    Database.getInstance().db.collection("profiles")
                            .whereEqualTo("email", invites.getText().toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    String id = queryDocumentSnapshots.toObjects(Profile.class).get(0).getId();
                                    pending.add(id);
                                    Toast.makeText(getActivity(), "User found, add successful. ID added: " + id, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "You can't invite yourself to your own party, dumbass.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                List<Address> addresses;

                try {
                    String address = location.getText().toString();
                    addresses = g.getFromLocationName(address, 5);

                    findLocation(addresses);

                    if (gmap != null) {
                        LatLng pos = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        gmap.clear();
                        gmap.addMarker(markerOptions.position(pos));
                        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, defaultZoom, 0, 0)));
                    }

                } catch (IOException e) {
                    Log.d("locationButton", e.getLocalizedMessage());
                    Toast.makeText(view.getContext(), "Please enter a valid location.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationArr.isEmpty())
                    cb.OnLocationInviteSaved(locationArr, pending);
                else
                    Toast.makeText(getContext(), "Please enter a valid location.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public void findLocation(List<Address> addresses) {
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getContext(), "Location not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // most recent location is saved
        if (locationArr.isEmpty()) {
            locationArr.add(addresses.get(0).getAddressLine(0));
            locationArr.add(addresses.get(0).getLocality());
            locationArr.add(StateAbbr.convert2Abbr(addresses.get(0).getAdminArea()));
        } else {
            locationArr.set(0, addresses.get(0).getAddressLine(0));
            locationArr.set(1, addresses.get(0).getLocality());
            locationArr.set(2, StateAbbr.convert2Abbr(addresses.get(0).getAdminArea()));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap = googleMap;

        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                gmap.clear();
                gmap.addMarker(markerOptions.position(latLng));
                gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, defaultZoom, 0, 0)));

                try {
                    List<Address> addresses = g.getFromLocation(latLng.latitude, latLng.longitude, 5);
                    findLocation(addresses);
                } catch (IOException e) {
                    Log.d("onMapClick", e.getLocalizedMessage());
                    Toast.makeText(getContext(), "Please enter a valid location.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

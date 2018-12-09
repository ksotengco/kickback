package edu.cs371m.kickback.page.userEvents;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.service.StateAbbr;


public class EventPage extends Fragment implements OnMapReadyCallback {

    private TextView address;
    private TextView eventName;
    private TextView eventDesc;

    private EditText editAddress;
    private EditText editEventName;
    private EditText editEventDesc;

    private Button editButton;
    private Button saveButton;

    private ViewSwitcher eventView;
    private ViewSwitcher descView;
    private ViewSwitcher addressView;
    private ViewSwitcher buttonView;

    private final ArrayList<String> locationArr = new ArrayList<>();
    private GeoPoint geoLocation;

    private GoogleMap gmap;
    private Geocoder  g;

    private boolean isEdit = false;

    private final float defaultZoom = 15.0f;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_page, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String hostID = getArguments().getString("hostId");

        address = v.findViewById(R.id.address_info);
        eventName = v.findViewById(R.id.event_name_info);
        eventDesc = v.findViewById(R.id.event_description);

        addressView = v.findViewById(R.id.address_view);
        eventView   = v.findViewById(R.id.event_view);
        descView    = v.findViewById(R.id.description_view);
        buttonView  = v.findViewById(R.id.button_view);

        editAddress = v.findViewById(R.id.edit_address_info);
        editEventName = v.findViewById(R.id.edit_event_name_info);
        editEventDesc = v.findViewById(R.id.edit_event_description);

        editButton = v.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchView();
            }
        });

        saveButton = v.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEvent();
                switchView();
            }
        });

        if (TextUtils.equals(hostID, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            editButton.setVisibility(View.VISIBLE);
            editButton.setClickable(true);
        }

        initText();

        return v;
    }

    private void editEvent() {
        DocumentReference eventListing = Database.getInstance().db.collection("events")
                .document(getArguments().getString("eventId"));

        eventListing.update("eventName", editEventName.getText().toString());
        eventListing.update("description", editEventDesc.getText().toString());

        if (!locationArr.isEmpty()) {
            eventListing.update("location", locationArr);
        }

        if (geoLocation != null) {
            eventListing.update("geolocation", geoLocation);
        }

        address.setText(editAddress.getText());
        eventName.setText(editEventName.getText());
        eventDesc.setText(editEventDesc.getText());
    }

    private void initText() {
        ArrayList<String> addressInfo = getArguments().getStringArrayList("location");

        if (addressInfo != null && !addressInfo.isEmpty()) {
            address.setText(getArguments().getStringArrayList("location").get(0));
            editAddress.setText(getArguments().getStringArrayList("location").get(0));
        } else {
            address.setText("N/A");
        }
        eventName.setText(getArguments().getString("eventName"));
        editEventName.setText(getArguments().getString("eventName"));

        eventDesc.setText(getArguments().getString("description"));
        editEventDesc.setText(getArguments().getString("description"));
    }

    private void switchView () {
        if (addressView != null)
            addressView.showNext();
        if (eventView != null)
            eventView.showNext();
        if (descView != null)
            descView.showNext();
        if (buttonView != null)
            buttonView.showNext();

        isEdit = !isEdit;
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
        g = new Geocoder(getContext());

        LatLng pos =  new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        gmap.clear();
        gmap.addMarker(new MarkerOptions().position(pos));
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, defaultZoom, 0, 0)));

        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            private MarkerOptions markerOptions;
            @Override
            public void onMapClick(LatLng latLng) {
                if (isEdit) {
                    if (markerOptions == null) {
                        markerOptions = new MarkerOptions();
                    }

                    gmap.clear();
                    gmap.addMarker(markerOptions.position(latLng));
                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, defaultZoom, 0, 0)));

                    try {
                        List<Address> addresses = g.getFromLocation(latLng.latitude, latLng.longitude, 5);
                        findLocation(addresses);

                        editAddress.setText(locationArr.get(0));

                        geoLocation = new GeoPoint(latLng.latitude, latLng.longitude);

                    } catch (IOException e) {
                        Log.d("onMapClick", e.getLocalizedMessage());
                        Toast.makeText(getContext(), "Please enter a valid location.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

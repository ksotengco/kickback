package edu.cs371m.kickback.page.userEvents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.cs371m.kickback.R;


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

    private GoogleMap gmap;

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
                switchView();
            }
        });

        if (TextUtils.equals(hostID, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            editButton.setVisibility(View.VISIBLE);
            editButton.setClickable(true);
        }

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

        return v;
    }

    public void switchView () {
        if (addressView != null)
            addressView.showNext();
        if (eventView != null)
            eventView.showNext();
        if (descView != null)
            descView.showNext();
        if (buttonView != null)
            buttonView.showNext();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap = googleMap;

        LatLng pos =  new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        gmap.clear();
        gmap.addMarker(new MarkerOptions().position(pos));
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, defaultZoom, 0, 0)));
    }
}

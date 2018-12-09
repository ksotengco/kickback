package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.cs371m.kickback.R;

public class EventPage extends Fragment implements OnMapReadyCallback {

    private TextView hostName;
    private TextView eventName;
    private TextView eventDesc;

    private GoogleMap gmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_page, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hostName = v.findViewById(R.id.host_name_info);
        eventName = v.findViewById(R.id.event_name_info);
        eventDesc = v.findViewById(R.id.event_description);

        hostName.setText(getArguments().getString("hostName"));
        eventName.setText(getArguments().getString("eventName"));
        eventDesc.setText(getArguments().getString("description"));

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap = googleMap;


    }
}

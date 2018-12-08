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

public class LocationAndInvite extends Fragment {

    OnButtonPressed cb;

    private EditText location;
    private EditText invites;

    private Button locationButton;
    private Button inviteButton;

    private Button nextButton;
    private Button prevButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_and_invite, container, false);

        cb = (OnButtonPressed) getActivity();

        final ArrayList<String> pending = new ArrayList<>();
        final ArrayList<String> locationArr = new ArrayList<>();

        location = (EditText) v.findViewById(R.id.editLocation);
        invites  = (EditText) v.findViewById(R.id.editInvites);

        locationButton = (Button) v.findViewById(R.id.locationButton);
        inviteButton   = (Button) v.findViewById(R.id.inviteButton);

        nextButton     = (Button) v.findViewById(R.id.next_button);
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
            private Geocoder g;
            @Override
            public void onClick(View view) {
                if (g == null) {
                    g = new Geocoder(view.getContext());
                }

                List<Address> addresses;

                try {
                    String address = location.getText().toString();
                    addresses = g.getFromLocationName(address, 5);

                    if (addresses == null || addresses.size() == 0) {
                        Toast.makeText(view.getContext(), "Location not found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // most recent location is saved
                    if (locationArr.isEmpty()) {
                        locationArr.add(address);
                    } else {
                        locationArr.set(0, address);
                    }

                } catch (IOException e) {
                    Log.d("locationButton", e.getLocalizedMessage());
                    Toast.makeText(view.getContext(), "Please enter a valid location.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb.OnLocationInviteSaved(locationArr, pending);
            }
        });

        return v;
    }


}

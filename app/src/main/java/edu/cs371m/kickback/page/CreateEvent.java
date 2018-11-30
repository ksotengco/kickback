package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Appitivty;
import edu.cs371m.kickback.listener.OnAddEventListener;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.model.Profile;

public class CreateEvent extends Fragment {

    private EditText editEventName;
    private EditText editDescription;

    private EditText editInvites;
    private Button inviteButton;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button createEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_event, container, false);
        final ArrayList<String> pending = new ArrayList<>();

        editEventName = v.findViewById(R.id.editEventName);
        editDescription = v.findViewById(R.id.editDescription);

        editInvites = v.findViewById(R.id.editInvites);
        inviteButton = v.findViewById(R.id.inviteButton);

        datePicker = v.findViewById(R.id.date_picker);
        timePicker = v.findViewById(R.id.time_picker);
        createEventButton = v.findViewById(R.id.createEventButton);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(editInvites.getText().toString())) {
                    Database.getInstance().db.collection("profiles")
                            .whereEqualTo("email", editInvites.getText().toString())
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

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emptyFields()) {
                    Bundle eventInfo = new Bundle();
                    eventInfo.putString("eventName", editEventName.getText().toString());
                    eventInfo.putString("description", editDescription.getText().toString());
                    eventInfo.putString("hostId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    eventInfo.putString("hostName", Appitivty.getCurrentProfile().getFirstName() + " " + Appitivty.getCurrentProfile().getLastName());

                    ArrayList<Integer> date = new ArrayList<Integer>();
                    date.add(datePicker.getDayOfMonth());
                    // indexing offset; month indexed starting at 0
                    date.add(datePicker.getMonth() + 1);
                    date.add(datePicker.getYear());

                    ArrayList<Integer> time = new ArrayList<Integer>();
                    // this follows 24-hour format
                    time.add(timePicker.getHour());
                    time.add(timePicker.getMinute());

                    eventInfo.putIntegerArrayList("date", date);
                    eventInfo.putIntegerArrayList("time", time);

                    eventInfo.putStringArrayList("pending", pending);

                    Event newEvent = new Event(eventInfo);
                    Database.getInstance().addEvent(newEvent, (OnAddEventListener) getActivity());
                } else {
                    Toast.makeText(getActivity(), "You forgot something.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }

    private boolean emptyFields() {
        return TextUtils.isEmpty(editEventName.getText().toString()) || TextUtils.isEmpty(editDescription.getText().toString());
    }
}

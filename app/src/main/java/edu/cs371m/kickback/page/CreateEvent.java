package edu.cs371m.kickback.page;

import android.app.DatePickerDialog;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

                    eventInfo.putString("date", createDate());

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

    private String createDate () {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, datePicker.getYear());
        // indexing offset; month indexed starting at 0
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        // follows 24-hour format
        cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        cal.set(Calendar.MINUTE, timePicker.getMinute());
        cal.set(Calendar.SECOND, 0);

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(cal.getTime());
    }

    private boolean emptyFields() {
        return TextUtils.isEmpty(editEventName.getText().toString()) || TextUtils.isEmpty(editDescription.getText().toString());
    }
}

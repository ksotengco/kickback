package edu.cs371m.kickback.page.searching;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

public class SearchResults extends Fragment {

    RecyclerView recyclerView;
    SearchFirestoreAdapter adapter;

    ImageButton calendarButton;
    ImageButton timeButton;

    Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_page, container, false);

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        calendarButton = v.findViewById(R.id.calendar_button);
        timeButton = v.findViewById(R.id.time_button);

        searchButton = v.findViewById(R.id.search_button);

        // https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                myCalendar.set(Calendar.SECOND, 0);
            }

        };

        final TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                myCalendar.set(Calendar.HOUR_OF_DAY, h);
                myCalendar.set(Calendar.MINUTE, m);
            }
        };

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(view.getContext(), timeListener, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if there's a better way to do this, let me know
                if (adapter != null) {
                    adapter.stopListening();
                }

                Query q = Database.getInstance().db.collection("events")
                        .whereGreaterThanOrEqualTo("date", createDate(myCalendar)).orderBy("date");

                FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                        .setQuery(q, Event.class)
                        .build();

                adapter = new SearchFirestoreAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
        });

        return v;
    }

    private String createDate (Calendar myCalendar) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Log.d("searchCreateDate", formatter.format(myCalendar.getTime()));
        return formatter.format(myCalendar.getTime());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}

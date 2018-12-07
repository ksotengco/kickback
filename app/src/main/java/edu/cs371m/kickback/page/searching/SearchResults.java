package edu.cs371m.kickback.page.searching;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Calendar;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

public class SearchResults extends Fragment {

    RecyclerView recyclerView;
    SearchFirestoreAdapter adapter;

    ImageButton calendarButton;
    ImageButton timeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_page, container, false);

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        calendarButton = v.findViewById(R.id.calendar_button);
        timeButton = v.findViewById(R.id.time_button);

        // https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(Database.getInstance().db.collection("events"), Event.class)
                .build();

        adapter = new SearchFirestoreAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

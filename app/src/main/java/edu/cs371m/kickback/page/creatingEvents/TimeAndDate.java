package edu.cs371m.kickback.page.creatingEvents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import edu.cs371m.kickback.R;

public class TimeAndDate extends Fragment {

    OnButtonPressed cb;

    private DatePicker date;
    private TimePicker time;

    private Button nextButton;
    private Button prevButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_and_date, container, false);

        cb = (OnButtonPressed) getActivity();

        date = (DatePicker) v.findViewById(R.id.date_picker);
        time = (TimePicker) v.findViewById(R.id.time_picker);

        nextButton = (Button) v.findViewById(R.id.next_button);
        prevButton = (Button) v.findViewById(R.id.prev_button);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb.prevPage();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb.OnTimeDateSaved(createDate());
            }
        });

        return v;
    }

    private String createDate () {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, date.getYear());
        // indexing offset; month indexed starting at 0
        cal.set(Calendar.MONTH, date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

        // follows 24-hour format
        cal.set(Calendar.HOUR_OF_DAY, time.getHour());
        cal.set(Calendar.MINUTE, time.getMinute());
        cal.set(Calendar.SECOND, 0);

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(cal.getTime());
    }
}

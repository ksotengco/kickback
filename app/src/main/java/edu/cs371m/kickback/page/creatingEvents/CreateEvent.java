package edu.cs371m.kickback.page.creatingEvents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Appitivty;
import edu.cs371m.kickback.listener.OnAddEventListener;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;

interface OnButtonPressed {
    void OnNameDescSaved(String name, String desc);
    void OnTimeDateSaved(String date);
    void OnLocationInviteSaved( ArrayList<String> location, double[] geolocation, ArrayList<String> pending);
}

// https://developer.android.com/training/animation/screen-slide
public class CreateEvent extends AppCompatActivity implements OnButtonPressed, OnAddEventListener {

    private static final int PAGES = 3;

    private CustomViewPager createEvent;
    private PagerAdapter pagerAdapter;

    private Bundle eventInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        createEvent = (CustomViewPager) findViewById(R.id.create_event_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        createEvent.setAdapter(pagerAdapter);

        eventInfo = new Bundle();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void nextPage() {
        if (createEvent.getCurrentItem() < 3)
            createEvent.setCurrentItem(createEvent.getCurrentItem() + 1);
    }

    public void saveEvent() {
        Event newEvent = new Event(eventInfo);
        Database.getInstance().addEvent(newEvent, (OnAddEventListener) this);
    }

    @Override
    public void onAddEvent(Event event) {
        Log.d("EVENT READY", "onEventReady: " + event.getDescription());
        for (String id : event.getPending()) {
            Database.getInstance().addInvite(id, event.getEventId());
        }

        finish();

        /*Bundle eventInfo = new Bundle();
        eventInfo.putString("host_name", event.getHostName());
        eventInfo.putString("event_name", event.getEventName());
        eventInfo.putString("event_desc", event.getDescription());

        EventPage goToEvent = new EventPage();
        goToEvent.setArguments(eventInfo);



        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.app_fragment, goToEvent)
                .commit();*/
    }

    @Override
    public void OnNameDescSaved(String name, String desc) {
        eventInfo.putString("eventName", name);
        eventInfo.putString("description", desc);

        eventInfo.putString("hostId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        eventInfo.putString("hostName", Appitivty.getCurrentProfile().getFirstName() + " " + Appitivty.getCurrentProfile().getLastName());

        nextPage();
    }

    @Override
    public void OnTimeDateSaved(String date) {
        eventInfo.putString("date", date);

        nextPage();
    }

    @Override
    public void OnLocationInviteSaved(ArrayList<String> location, double[] geolocation, ArrayList<String> pending) {
        eventInfo.putStringArrayList("location", location);
        eventInfo.putDoubleArray("geolocation", geolocation);
        eventInfo.putStringArrayList("pending", pending);

        saveEvent();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new NameAndDescription();
                case 1:
                    return new TimeAndDate();
                case 2:
                    return new LocationAndInvite();
                default:
                    finish();
            }

            // TODO: figure what to do on error
            return null;
        }

        @Override
        public int getCount() {
            return PAGES;
        }
    }
}

package edu.cs371m.kickback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.listener.OnAddEventListener;
import edu.cs371m.kickback.listener.OnAddProfileListener;
import edu.cs371m.kickback.listener.OnGetProfileListener;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.page.EventInvites;
import edu.cs371m.kickback.page.EventPage;
import edu.cs371m.kickback.page.HomePage;
import edu.cs371m.kickback.service.Database;

public class Appitivty extends AppCompatActivity implements OnAddEventListener {

    private DrawerLayout drawerLayout;
    private NavigationView mainNav;
    private static Profile currentProfile;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Create activity, toolbar, and inflate layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apptivity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        mainNav = findViewById(R.id.mainNav);

        Intent caller = getIntent();
        final Bundle userInfo = caller.getBundleExtra("userInfo");

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TOKEN", task.getResult().getToken());
                    token = task.getResult().getToken();
                    if (userInfo != null) {
                        Profile newProfile = new Profile(
                                userInfo.getString("id"),
                                userInfo.getString("email"),
                                userInfo.getString("firstName"),
                                userInfo.getString("lastName"));

                        newProfile.setActive(true);
                        newProfile.setDeviceToken(token);

                        Database.getInstance().addProfile(newProfile, new OnAddProfileListener() {
                            @Override
                            public void onAddProfile(Profile newProfile) {
                                currentProfile = newProfile;

                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.app_fragment, new HomePage(), "HOME_PAGE")
                                        .commit();
                            }
                        });
                    } else {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("active", true);
                        updates.put("deviceToken", token);
                        Database.getInstance().signInProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(), updates,
                                new OnGetProfileListener() {
                                    @Override
                                    public void onGetProfile(Profile profile) {
                                        currentProfile = profile;

                                        getSupportFragmentManager().beginTransaction()
                                                .add(R.id.app_fragment, new HomePage(), "HOME_PAGE")
                                                .commit();
                                    }
                                });
                    }
                }
            }
        });



        mainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.app_fragment, new HomePage())
                                .commit();
                        return true;
                    case R.id.nav_invites:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.app_fragment, new EventInvites())
                                .commit();
                        return true;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent signOutIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(signOutIntent);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }

    public static Profile getCurrentProfile() {
        return currentProfile;
    }

    @Override
    public void onAddEvent(final Event event) {
        Log.d("EVENT READY", "onEventReady: " + event.getDescription());
        for (String id : event.getPending()) {
            Database.getInstance().addInvite(id, event.getEventId());
        }

        Bundle eventInfo = new Bundle();
        eventInfo.putString("host_name", event.getHostName());
        eventInfo.putString("event_name", event.getEventName());
        eventInfo.putString("event_desc", event.getDescription());

        EventPage goToEvent = new EventPage();
        goToEvent.setArguments(eventInfo);

        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.app_fragment, goToEvent)
                .commit();
    }
}

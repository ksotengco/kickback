package edu.cs371m.kickback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.page.EventInvites;
import edu.cs371m.kickback.page.HomePage;

// callback for getting and adding profile
interface WaitForDataQuery {
    void onProfileReady(Profile profile);
    void onEventReady(Event event);
}

public class Appitivty extends AppCompatActivity implements WaitForDataQuery {

    private DrawerLayout drawerLayout;
    private NavigationView mainNav;
    private static Profile currentProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Create activity, toolbar, and inflate layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apptivity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        mainNav = findViewById(R.id.mainNav);
        Database.setCallback(this);

        Intent caller = getIntent();
        Bundle logInfo = caller.getBundleExtra("info");

        if (logInfo != null) {
            Database.getInstance().addProfile(FirebaseAuth.getInstance().getCurrentUser(), logInfo);
        } else {
            Database.getInstance().getProfile(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        //Log.d("Appitivity", "Profile: " + currentProfile.getFirstName());

        mainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent signOutIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(signOutIntent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_invites) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.app_fragment, new EventInvites())
                            .commit();
                }
                return false;
            }
        });

        // Get user info from database and store the user in Auth instance?
    }

    public static Profile getCurrentProfile() {
        return currentProfile;
    }

    @Override
    public void onProfileReady(Profile profile) {
        currentProfile = profile;

        // TODO: figure out notifications
        Database.getInstance().db.collection("profiles").document(profile.getId()).collection("invites").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<DocumentChange> inviteChanges = queryDocumentSnapshots.getDocumentChanges();
                    Map<String, Object> viewedMap;

                    int numInvites = 0;
                    for (DocumentChange d : inviteChanges) {
                        viewedMap = d.getDocument().getData();

                        if (d.getType() == DocumentChange.Type.ADDED && viewedMap.get("viewed").equals(false)) {
                            Log.d("DocumentChange2", d.getDocument().getId());
                            ++numInvites;
                        }
                    }

                    if (numInvites > 0) {
                        Toast.makeText(getApplicationContext(), "You have " + numInvites + " unread invites!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.app_fragment, new HomePage(), "HOME_PAGE")
                .commit();
    }

    @Override
    public void onEventReady(final Event event) {
        Log.d("EVENT READY", "onEventReady: " + event.getDescription());
        for (String id : event.getPending()) {
            Database.getInstance().addInvite(id, event.getEventId());
        }
    }
}

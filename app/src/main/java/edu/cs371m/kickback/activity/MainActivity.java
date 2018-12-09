package edu.cs371m.kickback.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs371m.kickback.listener.OnGetProfilesListener;
import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.page.LandingPage;
import edu.cs371m.kickback.R;
import edu.cs371m.kickback.service.Database;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        ImageButton menu = findViewById(R.id.menuButton);
        menu.setVisibility(View.INVISIBLE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

        Log.d("ON CREATE", "aaaaa");
        userAuth = FirebaseAuth.getInstance();
        userAuth.signOut();
        FirebaseUser currentUser = userAuth.getCurrentUser();

        if (currentUser != null) {
            // redirect to home page
            startApptivity(null);
        } else {
            Log.d("hahaha", "hahahaah");
            Database.getInstance()
                    .getProfiles(new OnGetProfilesListener() {
                        @Override
                        public void onGetProfiles(ArrayList<Profile> profiles) {
                            LandingPage landingPage = new LandingPage();
                            HashMap<String, Boolean> emailMap = new HashMap<>();
                            for (Profile p : profiles) {
                                emailMap.put(p.getEmail(), false);
                            }
                            landingPage.setEmails(emailMap);
                            Log.d("MAIN", "aaaaa");
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.main_fragment, landingPage, "LANDING_PAGE")
                                    .commit();
                        }
                    });
        }
    }

    // redirects to home page
    public void startApptivity(Bundle userInfo) {
        Intent startApp = new Intent(this, Appitivty.class);

        if (userInfo != null) {
            startApp.putExtra("userInfo", userInfo);
        }
        
        startActivity(startApp);

        Log.d("MAIN", "startApptivity: ");
        finish();
    }
}

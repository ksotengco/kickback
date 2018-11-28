package edu.cs371m.kickback.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.page.LandingPage;
import edu.cs371m.kickback.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

        userAuth = FirebaseAuth.getInstance();
        //userAuth.signOut();
        FirebaseUser currentUser = userAuth.getCurrentUser();

        if (currentUser != null) {
            // redirect to home page
            startApptivity(null);
        } else {
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.main_fragment, new LandingPage(), "LANDING_PAGE")
                                       .commit();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TOKEN", task.getResult().getToken());
                }
            }
        });
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

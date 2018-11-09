package edu.cs371m.kickback.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cs371m.kickback.page.LandingPage;
import edu.cs371m.kickback.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = userAuth.getCurrentUser();

        if (currentUser != null) {
            // redirect to home page
            startApptivity();
        } else {
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.main_fragment, new LandingPage(), "LANDING_PAGE")
                                       .commit();
        }
    }

    public void startApptivity() {
        Intent startApp = new Intent(this, Appitivty.class);
        startActivity(startApp);
        Log.d("MAIN", "startApptivity: ");
        finish();
    }
}

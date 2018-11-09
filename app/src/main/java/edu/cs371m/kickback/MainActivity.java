package edu.cs371m.kickback;

import android.app.FragmentManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        userAuth = FirebaseAuth.getInstance();
        userAuth.signOut();
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

    public static void startApptivity() {
        Intent startApp = new Intent(context, Appitivty.class);
        context.startActivity(startApp);
    }
}

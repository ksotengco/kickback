package edu.cs371m.kickback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.page.HomePage;

public class Appitivty extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView mainNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Create activity, toolbar, and inflate layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apptivity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        mainNav = findViewById(R.id.mainNav);
        mainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent signOutIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(signOutIntent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Get user info from database and store the user in Auth instance?

        getSupportFragmentManager().beginTransaction()
                                    .add(R.id.app_fragment, new HomePage(), "HOME_PAGE")
                                    .commit();
    }
}

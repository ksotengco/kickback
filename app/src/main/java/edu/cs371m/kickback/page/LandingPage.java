package edu.cs371m.kickback.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Database;
import edu.cs371m.kickback.activity.MainActivity;

import static android.app.Activity.RESULT_OK;

public class LandingPage extends Fragment {

    private Button signUpButton;
    private Button signInButton;
    private final String TAG = "LANDING_PAGE_FRAG";
    final int RC_SIGN_IN = 1;

    // WITCHELL
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_page, container, false);

        signUpButton = v.findViewById(R.id.signUpButton);
        signInButton = v.findViewById(R.id.signInButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new SignUp())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Adapted from FC6
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(), RC_SIGN_IN);
            }
        });

        return v;
    }

    // WITCHEL Adapted from FC6
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    ((MainActivity)getActivity()).startApptivity(null);
                }
            }
        }
    }
}

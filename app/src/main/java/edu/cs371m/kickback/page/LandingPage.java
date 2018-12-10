package edu.cs371m.kickback.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.MainActivity;
import edu.cs371m.kickback.listener.OnGetProfilesListener;
import edu.cs371m.kickback.model.Profile;
import edu.cs371m.kickback.service.Database;

import static android.app.Activity.RESULT_OK;

public class LandingPage extends Fragment {

    private Button continueButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progress;
    private Map<String, Profile> emailMap;
    boolean reloadProfiles;
    private final String TAG = "LANDING_PAGE_FRAG";
    final int RC_SIGN_IN = 1;

    public void setEmailProfileMap(Map <String, Profile> emailMap) {
        this.emailMap = emailMap;
    }

    // WITCHELL
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_page, container, false);

        continueButton = v.findViewById(R.id.button);
        emailEditText = v.findViewById(R.id.emailEditText);
        passwordEditText = v.findViewById(R.id.passwordEditText);
        progress = v.findViewById(R.id.progressCircle);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reloadProfiles) {
                    Database.getInstance().getProfiles(new OnGetProfilesListener() {
                        @Override
                        public void onGetProfiles(ArrayList<Profile> profiles) {
                            HashMap<String, Profile> emailMap = new HashMap<>();
                            for (Profile p : profiles) {
                                emailMap.put(p.getEmail(), p);
                            }
                            setEmailProfileMap(emailMap);
                            attemptToContinue();
                        }
                    });
                } else {
                    attemptToContinue();
                }

            }
        });

        return v;
    }

    public void attemptToContinue() {
        String emailValue = emailEditText.getText().toString();
        String passValue = passwordEditText.getText().toString();

        if (emailValue != null && Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            if (passValue != null && passValue.length() >= 6) {
                continueButton.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
                Profile profile = emailMap.getOrDefault(emailValue, null);
                if (profile == null) {
                    // sign up
                    ProfilePage signUp = new ProfilePage();
                    Bundle userInfo = new Bundle();
                    userInfo.putString("email", emailValue);
                    userInfo.putString("pass", passValue);
                    userInfo.putBoolean("newUser", true);
                    signUp.setArguments(userInfo);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.main_fragment, signUp)
                            .commit();
                } else {
                    // sign in
                    if (profile.isActive()) {
                        continueButton.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getContext(), "ERROR: This account is signed in on another device. Please sign out on the other device and try again.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        reloadProfiles = true;
                    } else {
                        FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(emailValue, passValue)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            ((MainActivity) getActivity()).startApptivity(null);
                                        }
                                    }
                                });
                    }
                }
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "Required:\nEmail must be valid format\nPassword min length is 6", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}

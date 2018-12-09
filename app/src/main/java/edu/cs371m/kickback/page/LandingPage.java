package edu.cs371m.kickback.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.MainActivity;

import static android.app.Activity.RESULT_OK;

public class LandingPage extends Fragment {

    private Button continueButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progress;
    private Map<String, Boolean> emailMap;
    private final String TAG = "LANDING_PAGE_FRAG";
    final int RC_SIGN_IN = 1;

    public void setEmails(Map <String, Boolean> emailMap) {
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
                String emailValue = emailEditText.getText().toString();
                String passValue = passwordEditText.getText().toString();

                if (emailValue != null && Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    if (passValue != null && passValue.length() >= 6) {
                        continueButton.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        boolean newUser = emailMap.getOrDefault(emailValue, true);
                        if (newUser) {
                            // sign up
                            SignUp signUp = new SignUp();
                            Bundle userInfo = new Bundle();
                            userInfo.putString("email", emailValue);
                            userInfo.putString("pass", passValue);
                            signUp.setArguments(userInfo);
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.main_fragment, new SignUp())
                                    .commit();
                        } else {
                            // sign in
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
                } else {
                    TextView error = new TextView(getContext());
                    Toast toast = Toast.makeText(getContext(), "Required:\nEmail must be valid format\nPassword min length is 6", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
            }
        });

//        emailEditText.keyboardCloseOnDone();
//        passwordEditText.keyboardCloseOnDone();

//        TextInputLayout t = v.findViewById(R.id.t);
//        t.setError("ERROR");
//        t.setHint("DNJSAKDLASNJK");
//        button = v.findViewById(R.id.button);
//        appToast = v.findViewById(R.id.errorText);
//        button.setText("CONTINUE");
//        emailEditText = v.findViewById(R.id.emailEditText);
//        passwordEditText = v.findViewById(R.id.passwordEditText);
//
//        emailEditText.setValidator(new FieldValidator() {
//            @Override
//            public boolean isValid() {
//                return Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches();
//            }
//
//            @Override
//            public boolean onInvalid() {
//                validEmail = false;
//                return true;
//            }
//
//            @Override
//            public boolean onValid() {
//                validEmail = true;
//                return passwordEditText.performClick();
//            }
//        });
//
//        passwordEditText.setValidator(new FieldValidator() {
//            @Override
//            public boolean isValid() {
//                return passwordEditText.getText().toString().length() > 6;
//            }
//
//            @Override
//            public boolean onInvalid() {
//                validPass = false;
//                return false;
//            }
//
//            @Override
//            public boolean onValid() {
//                validPass = true;
//                return false;
//            }
//        });
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean validEmail = emailEditText.isValid();
//                boolean validPassword = passwordEditText.isValid();
//                if (validEmail && validPassword) {
//                    // Sign in/sign up
//                } else if (!validEmail){
//                    Toast toast = Toast.makeText(getContext(), "Email format is invalid", Toast.LENGTH_LONG);
//                } else {
//                    appToast.displayError("Password must be at least 6 characters", 2000, getActivity());
//                }
//            }
//        });



//        emailEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                emailEditText.setCursorVisible(true);
//                emailEditText.setTextColor(Color.BLACK);
//            }
//        });
//
//        View.OnFocusChangeListener hideKeyboardOnFocusChange = new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//                    hideKeyboard(view);
//                }
//            }
//        };
//
//        emailEditText.setOnFocusChangeListener(hideKeyboardOnFocusChange);
//        passwordEditText.setOnFocusChangeListener(hideKeyboardOnFocusChange);


//        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                String value = textView.getText().toString();
//
//                // Enter or done was pressed
//                if (keyEvent == null) {
//                    if (value != null && Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
//                        passwordEditText.performClick();
//                    }
//                }
//                return false;
//            }
//        });
//
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                String value = textView.getText().toString();
//                if
//                return false;
//            }
//        });
//
//        button.setOnClickListener();
//
//        button.
//
//        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                String value = textView.getText().toString();
//                Log.d(TAG, "email editor action: " + textView.getText().toString());
//                emailEditText.setCursorVisible(false);
//                if (value != null && Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
//                    boolean newUser = emailMap.getOrDefault(textView.getText().toString(), true);
//                    if (newUser) {
//                        if (button.getVisibility() == View.GONE) {
//                            button.setAlpha(0);
//                            button.setText("SIGN UP");
//                            button.setVisibility(View.VISIBLE);
//                            button.animate().alpha(1.0f).setDuration(1000).start();
//                        }
//                    } else {
//                        if (button.getVisibility() != View.VISIBLE) {
//
//                        }
//                        button.setAlpha(0);
//                        button.setText("SIGN IN");
//                        button.setVisibility(View.VISIBLE);
//                        button.animate().(1.0f).setDuration(1000).start();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Email format is invalid", Toast.LENGTH_SHORT).show();
//                    emailEditText.setTextColor(Color.RED);
//                    button.setVisibility(View.GONE);
//                }
//                return false;
//            }
//        });

//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.main_fragment, new SignUp())
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//
//        // Adapted from FC6
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<AuthUI.IdpConfig> providers = Arrays.asList(
//                        new AuthUI.IdpConfig.EmailBuilder().build());
//
//                // Create and launch sign-in intent
//                startActivityForResult(AuthUI.getInstance()
//                                .createSignInIntentBuilder()
//                                .setAvailableProviders(providers)
//                                .build(), RC_SIGN_IN);
//            }
//        });

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

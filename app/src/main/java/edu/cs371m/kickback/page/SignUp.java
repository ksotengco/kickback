package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Database;
import edu.cs371m.kickback.activity.MainActivity;
import edu.cs371m.kickback.model.Profile;

public class SignUp extends Fragment {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button signUpButton;
    private FirebaseAuth userAuth;
    private final String TAG = "SIGN_UP_FRAG";

    // ADAPTED FROM FC3
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_up, container, false);
        firstNameEdit = v.findViewById(R.id.editFirstName);
        lastNameEdit = v.findViewById(R.id.editLastName);

        emailEdit = v.findViewById(R.id.editEmail);
        passwordEdit = v.findViewById(R.id.editPassword);

        signUpButton = v.findViewById(R.id.confirmButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // quick error checking, probably gonna change later
                if (TextUtils.isEmpty(firstNameEdit.getText().toString()) ||
                        TextUtils.isEmpty(lastNameEdit.getText().toString())) {
                    Toast.makeText(view.getContext(), "Please enter in your first and last name", Toast.LENGTH_SHORT).show();
                } else {

                    userAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: " + "SUCCEEDED");
                                        Bundle logInfo = new Bundle();
                                        logInfo.putString("firstName", firstNameEdit.getText().toString());
                                        logInfo.putString("lastName", lastNameEdit.getText().toString());

                                        Database.getInstance().addProfile((MainActivity) getActivity(),
                                                FirebaseAuth.getInstance().getCurrentUser(), logInfo);

                                        //((MainActivity) getActivity()).startApptivity();
                                    } else {
                                        Log.d(TAG, "onComplete: " + "FAILED");
                                    }
                                }
                            });
                }
            }
        });

        userAuth = FirebaseAuth.getInstance();
        return v;
    }
}

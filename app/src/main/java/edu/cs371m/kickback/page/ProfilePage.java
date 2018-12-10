package edu.cs371m.kickback.page;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.MainActivity;
import edu.cs371m.kickback.service.Database;

public class ProfilePage extends Fragment {

    private ViewSwitcher switcher;
    private ImageView editProfilePic;
    private EditText picUrlEdit;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button saveButton;
    private Button applyPic;
    private Button revertPic;

    private FloatingActionButton editProfileButton;
    private TextView nameView;
    private TextView emailView;
    private ImageView profilePic;
    private final String TAG = "SIGN_UP_FRAG";

    // ADAPTED FROM FC3
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_page, container, false);

        switcher = v.findViewById(R.id.viewSwitcher1);
        LinearLayout viewProfile = v.findViewById(R.id.view1);
        LinearLayout editProfile = v.findViewById(R.id.view2);
        editProfileButton = v.findViewById(R.id.editProfileButton);

        Bundle userInfo = getArguments();
        boolean newUser = userInfo.getBoolean("newUser");
        if (newUser) {
            editProfile(v, userInfo);
        } else {
            viewProfile(v, userInfo, false);
        }

        return v;
    }

    public void viewProfile(final View v, final Bundle userInfo, boolean returnFromEdit) {

        if(returnFromEdit) {
            switcher.showPrevious();
        }
        nameView = v.findViewById(R.id.nameView);
        emailView = v.findViewById(R.id.emailView);
        profilePic = v.findViewById(R.id.profilePic);

        nameView.setText(userInfo.getString("firstName") + " " + userInfo.getString("lastName"));
        emailView.setText(userInfo.getString("email"));
        if (userInfo.getString("picUrl") == null) {
            Glide.with(getContext()).load(R.drawable.profile_pic_placeholder).into(profilePic);

        } else {
            Glide.with(getContext()).load(userInfo.getString("picUrl")).into(profilePic);
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(v, userInfo);
            }
        });
    }

    public void editProfile(final View v, final Bundle userInfo) {


        // switch to edit view
        switcher.showNext();

        // Get all views in edit view
        firstNameEdit = v.findViewById(R.id.firstNameEdit);
        lastNameEdit = v.findViewById(R.id.lastNameEdit);
        editProfilePic = v.findViewById(R.id.editProfilePic);
        picUrlEdit = v.findViewById(R.id.picUrlEdit);
        applyPic = v.findViewById(R.id.applyButton);
        revertPic = v.findViewById(R.id.revertButton);
        saveButton = v.findViewById(R.id.saveButton);

        // Prepopulate fields
        firstNameEdit.setText(userInfo.getString("firstName"));
        lastNameEdit.setText(userInfo.getString("lastName"));
        picUrlEdit.setText(userInfo.getString("picUrl"));


        if (userInfo.getString("picUrl") == null) {
            Glide.with(getContext()).load(R.drawable.profile_pic_placeholder).into(editProfilePic);
            picUrlEdit.setText(null);
        } else {
            Glide.with(getContext()).load(userInfo.getString("picUrl")).into(editProfilePic);
            picUrlEdit.setText(userInfo.getString("picUrl"));
        }

        applyPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getContext()).load(picUrlEdit.getText().toString())
                                .apply(new RequestOptions().placeholder(R.drawable.profile_pic_placeholder)
                                        .error(R.drawable.profile_pic_placeholder)).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Toast.makeText(getContext(), "Could not load image.", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(editProfilePic);
            }
        });

        revertPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInfo.getString("picUrl") == null) {
                    Glide.with(getContext()).load(R.drawable.profile_pic_placeholder).into(editProfilePic);
                    picUrlEdit.setText(null);
                } else {
                    Glide.with(getContext()).load(userInfo.getString("picUrl")).into(editProfilePic);
                    picUrlEdit.setText(userInfo.getString("picUrl"));
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameEdit.getText().toString();
                final String lastName = lastNameEdit.getText().toString();
                final String picUrlString = picUrlEdit.getText().toString();

                if (firstName.equals("") || lastName.equals("")) {
                    Toast.makeText(getContext(), "Please enter your first and last name.", Toast.LENGTH_SHORT).show();
                } else {
                    if (userInfo.getBoolean("newUser")){
                        FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(userInfo.getString("email"), userInfo.getString("pass"))
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Bundle userInfo = new Bundle();
                                            userInfo.putString("firstName", firstName);
                                            userInfo.putString("lastName", lastName);
                                            userInfo.putString("picUrl", picUrlString);
                                            userInfo.putString("id", task.getResult().getUser().getUid());
                                            userInfo.putString("email", task.getResult().getUser().getEmail());

                                            ((MainActivity) getActivity()).startApptivity(userInfo);
                                        }
                                    }
                                });
                    } else {
                        Map<String, Object> profileUpdates = new HashMap<>();
                        profileUpdates.put("firstName", firstName);
                        profileUpdates.put("lastName", lastName);
                        profileUpdates.put("profilePicUrl", picUrlString);
                        Database.getInstance().db.collection("profiles")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(profileUpdates, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Bundle bundle = new Bundle();
                                bundle.putString("firstName", firstName);
                                bundle.putString("lastName", lastName);
                                bundle.putString("picUrl", picUrlString);
                                bundle.putString("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                viewProfile(v, bundle, true);
                            }
                        });
                    }
                }
            }
        });

    }
}

package edu.cs371m.kickback.page.creatingEvents;

import android.os.Bundle;
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

import edu.cs371m.kickback.R;

public class NameAndDescription extends Fragment {

    OnButtonPressed cb;

    private EditText eventName;
    private EditText eventDesc;
    private EditText photoURL;

    private Button nextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.name_and_description, container, false);
        cb = (OnButtonPressed) getActivity();

        eventName = (EditText) v.findViewById(R.id.editEventName);
        eventDesc = (EditText) v.findViewById(R.id.editDescription);
        photoURL  = (EditText) v.findViewById(R.id.editPhotoURL);

        nextButton = (Button) v.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(eventName.getText().toString()) && !TextUtils.isEmpty(eventDesc.getText().toString()))
                    cb.OnNameDescSaved(eventName.getText().toString(), eventDesc.getText().toString(), photoURL.getText().toString());
                else
                    Toast.makeText(view.getContext(), "Please enter in the required fields.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}

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
import android.widget.TextView;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Appitivty;
import edu.cs371m.kickback.page.creatingEvents.CreateEvent;
import edu.cs371m.kickback.page.searching.SearchResults;

public class HomePage extends Fragment {

    private Button hostButton;
    private Button attendButton;
    private Button eventsButton;

    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page, container, false);

        // Purely for test purposes; delete later
        // ----------------------------------------------------------------------------
        String name = Appitivty.getCurrentProfile().getFirstName() + " " + Appitivty.getCurrentProfile().getLastName();

        TextView tv = v.findViewById(R.id.username_text);
        tv.setText(name);
        // ----------------------------------------------------------------------------

        hostButton = v.findViewById(R.id.hostShortcut);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), CreateEvent.class);
                getActivity().startActivity(in);
                /* getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.app_fragment, new CreateEvent())
                        .commit();*/
            }
        });

        attendButton = v.findViewById(R.id.attendShortcut);
        eventsButton = v.findViewById(R.id.myEventsShortcut);

        searchButton = v.findViewById(R.id.searchEvents);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.app_fragment, new SearchResults())
                        .commit();
            }
        });

        return v;
    }
}

package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.InviteFirestoreAdapter;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

public class SearchPage extends Fragment {

    RecyclerView recyclerView;
    SearchFirestoreAdapter adapter;
    TextView emailSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_page, container, false);

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        emailSearch = v.findViewById(R.id.search_by_email);
        // https://stackoverflow.com/questions/1489852/android-handle-enter-in-an-edittext
        emailSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER
                        && emailSearch.getText().length() > 0) {

                    String query = emailSearch.getText().toString();

                    FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                            .setQuery(Database.getInstance().db.collection("events")
                            .whereGreaterThanOrEqualTo("hostName", query), Event.class)
                            .build();

                    adapter = new SearchFirestoreAdapter(options);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                    return true;
                }

                return false;
            }
        });




        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

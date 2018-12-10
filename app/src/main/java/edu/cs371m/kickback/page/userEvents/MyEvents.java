package edu.cs371m.kickback.page.userEvents;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

public class MyEvents extends Fragment {

    private RecyclerView recyclerView;
    private SearchFirestoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_events, container, false);

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query q = Database.getInstance().db.collection("events")
                .whereEqualTo("hostId", FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("date");

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(q, Event.class)
                .build();

        adapter = new SearchFirestoreAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}

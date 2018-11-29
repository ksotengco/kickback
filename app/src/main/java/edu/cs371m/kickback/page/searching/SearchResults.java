package edu.cs371m.kickback.page.searching;

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

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.view.SearchFirestoreAdapter;

public class SearchResults extends Fragment {

    RecyclerView recyclerView;
    SearchFirestoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_page, container, false);

        recyclerView = v.findViewById(R.id.event_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(Database.getInstance().db.collection("events"), Event.class)
                .build();

        adapter = new SearchFirestoreAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

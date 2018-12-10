package edu.cs371m.kickback.page;

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

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.view.InviteFirestoreAdapter;

public class EventInvites extends Fragment {

    private RecyclerView recyclerView;
    private InviteFirestoreAdapter adapter;

    // WITCHELL FC7
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_invites, container, false);

        recyclerView = v.findViewById(R.id.inviteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(Database.getInstance().db.collection("events").whereArrayContains("pending", FirebaseAuth.getInstance().getCurrentUser().getUid()), Event.class)
                .build();
        adapter = new InviteFirestoreAdapter(options);
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

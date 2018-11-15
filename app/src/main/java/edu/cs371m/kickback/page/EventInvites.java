package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Database;
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

        // dbug
//        Database.getInstance().db.collection("events").whereArrayContains("pending", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("invites query", "onComplete: " + task.getResult().getDocuments().size());
//                        }
//                    }
//                });

        // set up adapter

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

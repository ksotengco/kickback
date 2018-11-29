package edu.cs371m.kickback.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;

public class SearchFirestoreAdapter extends FirestoreRecyclerAdapter<Event, SearchFirestoreAdapter.SearchViewHolder> {

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView hostName;

        public SearchViewHolder(View theView) {
            super(theView);
            eventName = theView.findViewById(R.id.eventName);
            hostName = theView.findViewById(R.id.eventHost);
        }
    }

    public SearchFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
        Log.d("adapterConstructor", "InviteFirestoreAdapter: ");
        // TODO: sort invites by event date created
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Event model) {
        holder.eventName.setText(model.getEventName());
        holder.hostName.setText(model.getHostName());
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new SearchViewHolder(v);
    }


}
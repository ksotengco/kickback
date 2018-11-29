package edu.cs371m.kickback.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.service.Database;
import edu.cs371m.kickback.model.Event;

public class InviteFirestoreAdapter extends FirestoreRecyclerAdapter<Event, InviteFirestoreAdapter.InviteViewHolder> {

    public class InviteViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView hostName;
        ImageView accept;
        ImageView decline;

        public InviteViewHolder(View theView) {
            super(theView);
            eventName = theView.findViewById(R.id.eventName);
            hostName = theView.findViewById(R.id.eventHost);
            accept = theView.findViewById(R.id.acceptImage);
            decline = theView.findViewById(R.id.declineImage);
        }
    }

    public InviteFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
        Log.d("adapterConstructor", "InviteFirestoreAdapter: ");
        // TODO: sort invites by event date created
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position, @NonNull final Event model) {
        Log.d("BIND", "onBindViewHolder: " + model.getEventName());
        holder.eventName.setText(model.getEventName());
        holder.hostName.setText(model.getHostName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), model.getEventName(), Toast.LENGTH_SHORT).show();
                Database.getInstance().viewInvite(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        model.getEventId());
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Accepted " + model.getEventName(), Toast.LENGTH_SHORT).show();
                Database.getInstance().inviteAccept(model.getEventId(), Database.EventUpdates.ACCEPTED);
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Declined", Toast.LENGTH_SHORT).show();
                Database.getInstance().inviteAccept(model.getEventId(), Database.EventUpdates.DECLINED);
            }
        });
    }

    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        Log.d("create holder", "onCreateViewHolder: " + i);
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.invite_card, group, false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        // Called when there is an error getting a query snapshot. You may want to update
        // your UI to display an error message to the user.
        Log.d("dasnd", "Error " + e.getMessage());
    }
}

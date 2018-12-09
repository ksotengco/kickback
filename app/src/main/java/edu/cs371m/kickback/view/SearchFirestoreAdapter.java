package edu.cs371m.kickback.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.page.EventPage;

public class SearchFirestoreAdapter extends FirestoreRecyclerAdapter<Event, SearchFirestoreAdapter.SearchViewHolder> {

    private DateFormat string2Date;
    private DateFormat formatter;

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView hostName;
        TextView dateView;

        CardView cardView;

        public SearchViewHolder(View theView) {
            super(theView);
            eventName = theView.findViewById(R.id.eventName);
            hostName = theView.findViewById(R.id.eventHost);
            dateView = theView.findViewById(R.id.dateView);

            cardView = theView.findViewById(R.id.event_card);

            // date will be shown according to user's timezone (hopefully)
            string2Date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");

            // TODO: possibly add user preference to displaying date/time
            formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            formatter.setTimeZone(TimeZone.getDefault());
        }
    }

    public SearchFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
        Log.d("adapterConstructor", "InviteFirestoreAdapter: ");
        // TODO: sort invites by event date created
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, final int position, @NonNull Event model) {
        holder.eventName.setText(model.getEventName());
        holder.hostName.setText(model.getHostName());
        try {
            holder.dateView.setText(formatter.format(string2Date.parse(model.getDate())));
        } catch (java.text.ParseException e) {
            Log.d("onBindViewHolder", e.getLocalizedMessage());
            holder.dateView.setText(model.getDate());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = getItem(position);
                EventPage goToEvent = new EventPage();

                goToEvent.setArguments(eventBundler(event));
                ((AppCompatActivity)view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.app_fragment, goToEvent)
                        .commit();
                //Toast.makeText(view.getContext(), event.getEventName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);

        return new SearchViewHolder(v);
    }

    private Bundle eventBundler (Event event) {
        Bundle eventInfo = new Bundle();

        eventInfo.putString("eventName", event.getEventName());
        eventInfo.putString("description", event.getDescription());

        eventInfo.putString("hostId", event.getHostId());
        eventInfo.putString("hostName", event.getHostName());

        eventInfo.putString("date", event.getDate());

        eventInfo.putDouble("lat", event.getGeolocation().getLatitude());
        eventInfo.putDouble("lng", event.getGeolocation().getLongitude());

        eventInfo.putStringArrayList("location", event.getLocation());
        eventInfo.putStringArrayList("pending", event.getPending());

        return eventInfo;
    }

}

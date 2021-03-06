package edu.cs371m.kickback.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.model.Event;
import edu.cs371m.kickback.page.userEvents.EventPage;

public class SearchFirestoreAdapter extends FirestoreRecyclerAdapter<Event, SearchFirestoreAdapter.SearchViewHolder> {

    private DateFormat string2Date;
    private DateFormat formatter;

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView hostName;
        TextView dateView;
        TextView locationView;

        ImageView photo;

        CardView cardView;

        public SearchViewHolder(View theView) {
            super(theView);
            eventName = theView.findViewById(R.id.eventName);
            hostName  = theView.findViewById(R.id.eventHost);
            dateView  = theView.findViewById(R.id.dateView);
            locationView = theView.findViewById(R.id.locationView);

            photo     = theView.findViewById(R.id.event_image);

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
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull final Event model) {
        holder.eventName.setText(model.getEventName());
        holder.hostName.setText(model.getHostName());
        try {
            holder.dateView.setText(formatter.format(string2Date.parse(model.getDate())));
        } catch (java.text.ParseException e) {
            Log.d("onBindViewHolder", e.getLocalizedMessage());
            holder.dateView.setText(model.getDate());
        }
        holder.locationView.setText(model.getLocation().get(0));

        if (!TextUtils.isEmpty(model.getPhotoId())) {
            Glide.with(holder.cardView).load(model.getPhotoId()).into(holder.photo);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventPage goToEvent = new EventPage();

                goToEvent.setArguments(eventBundler(model));
                ((AppCompatActivity)view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.app_fragment, goToEvent)
                        .commit();
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

        eventInfo.putString("eventId", event.getEventId());

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

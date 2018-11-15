package edu.cs371m.kickback.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.cs371m.kickback.R;

public class EventPage extends Fragment {

    private TextView hostName;
    private TextView eventName;
    private TextView eventDesc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_page, container, false);
        hostName = v.findViewById(R.id.host_name_info);
        eventName = v.findViewById(R.id.event_name_info);
        eventDesc = v.findViewById(R.id.event_description);

        hostName.setText(getArguments().getString("host_name"));
        eventName.setText(getArguments().getString("event_name"));
        eventDesc.setText(getArguments().getString("event_desc"));

        return v;
    }
}

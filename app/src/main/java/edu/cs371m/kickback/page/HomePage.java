package edu.cs371m.kickback.page;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import edu.cs371m.kickback.R;
import edu.cs371m.kickback.activity.Appitivty;
import edu.cs371m.kickback.activity.MainActivity;
import edu.cs371m.kickback.page.creatingEvents.CreateEvent;
import edu.cs371m.kickback.page.searching.SearchResults;
import edu.cs371m.kickback.page.userEvents.MyEvents;

public class HomePage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page, container, false);
        return v;
    }
}
